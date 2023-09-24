//
// Created by 陈颂颂 on 2020/1/4.
//

#include "include/debug-check.h"
#include "include/utils.h"
#include "include/log.h"
//#include <stdio.h>
//#include <string.h>
#include <sys/ptrace.h>

#include <jni.h>
#include <sys/types.h>
#include <cstring>
#include <cstdio>
#include <android/log.h>
#include <unistd.h>
#include <pthread.h>
#include <cstdlib>
#include <elf.h>
#include <link.h>
#include <fcntl.h>
#include <cerrno>


//#include <sys/prctl.h>
//#include <linux/filter.h>
//#include <linux/seccomp.h>
//#include <semaphore.h>
//#include <syscall.h>
//#include <linux/audit.h>





const char MAPS_FILE[] = "/proc/self/maps";
const char TAG[] = "JNI";

// customized syscalls
extern "C" int my_read(int, void *, size_t);
extern "C" int
my_openat(int dirfd, const char *const __pass_object_size pathname, int flags, mode_t modes);
extern "C" long my_ptrace(int __request, ...);

// Our customized __set_errno_internal for syscall.S to use.
// we do not use the one from libc due to issue https://github.com/android/ndk/issues/1422
extern "C" long __set_errno_internal(int n) {
    errno = n;
    return -1;
}



#define BUFFER_LEN 512

#define TAG "carleen"

#define DEBUG

#ifdef DEBUG
#define LOGI(...) __android_log_print(ANDROID_LOG_INFO, TAG, __VA_ARGS__)
#define LOGW(...) __android_log_print(ANDROID_LOG_WARN, TAG, __VA_ARGS__)
#else
#define LOGW(...) ((void)0)
#define LOGI(...) ((void)0)
#endif


/**
 * 获取 TracerPid
 * @return
 */
int getTracerPid() {
    FILE *f = NULL;
    char path[BUF_SIZE_64] = {0};
    sprintf(path, "/proc/%d/status", getpid());
    char line[BUF_SIZE_256] = {0};
    f = fopen(path, "r");
    while (fgets(line, BUF_SIZE_256, f)) {
        if (strstr(line, "TracerPid")) {
            LOGI("TracerPid line: %s", line);
            int statue = atoi(&line[10]);
            LOGW("TracerPid: %d", statue);
            return statue;
        }
    }
    return -1;
}


/**
 * 每个进程只能被一个进程 trace
 * @return
 * @deprecated 失效，只能在 fork 单独的进程中使用
 */
int ptraceCheck() {
    int trace = ptrace(PTRACE_TRACEME, 0, 0, 0);
    LOGW("ptrace: %d", trace);
    return trace;
}

bool findFridaMaps(){
    char line[1024];
    bool flag = false;

    FILE *fp = fopen("/proc/self/maps", "r");
    while (fgets(line, sizeof(line), fp)) {
        if (strstr(line, "frida-agent")) {
            flag =  true;
        }
        else if (strstr(line, "frida-gadget")) {
            flag =  true;
        }
        if (flag){
            fclose(fp);
            return flag;
        }
    }
    fclose(fp);
    return flag;

}

int read_line(int fd, char *ptr, unsigned int maxlen, jboolean use_customized_syscall) {
    int n;
    long rc;
    char c;

    for (n = 1; n < maxlen; n++) {
        rc = use_customized_syscall ? my_read(fd, &c, 1) : read(fd, &c, 1);
        if (rc == 1) {
            *ptr++ = c;
            if (c == '\n')
                break;
        } else if (rc == 0) {
            if (n == 1)
                return 0;    /* EOF no data read */
            else
                break;    /* EOF, some data read */
        } else
            return (-1);    /* error */
    }
    *ptr = 0;
    return (n);
}

int wrap_endsWith(const char *str, const char *suffix) {
    if (!str || !suffix)
        return 0;
    size_t lenA = strlen(str);
    size_t lenB = strlen(suffix);
    if (lenB > lenA)
        return 0;
    return strncmp(str + lenA - lenB, suffix, lenB) == 0;
}

int elf_check_header(uintptr_t base_addr) {
    auto *ehdr = (ElfW(Ehdr) *) base_addr;
    if (0 != memcmp(ehdr->e_ident, ELFMAG, SELFMAG)) return 0;
#if defined(__LP64__)
    if (ELFCLASS64 != ehdr->e_ident[EI_CLASS]) return 0;
#else
    if (ELFCLASS32 != ehdr->e_ident[EI_CLASS]) return 0;
#endif
    if (ELFDATA2LSB != ehdr->e_ident[EI_DATA]) return 0;
    if (EV_CURRENT != ehdr->e_ident[EI_VERSION]) return 0;
    if (ET_EXEC != ehdr->e_type && ET_DYN != ehdr->e_type) return 0;
    if (EV_CURRENT != ehdr->e_version) return 0;
    return 1;
}

int find_mem_string(uint64_t base, uint64_t end, unsigned char *ptr, unsigned int len) {
    auto *rc = (unsigned char *) base;

    while ((uint64_t) rc < end - len) {
        if (*rc == *ptr) {
            if (memcmp(rc, ptr, len) == 0) {
                return 1;
            }
        }
        rc++;
    }
    return 0;
}


bool check_fridaRpc(JNIEnv *env,jstring signature) {

    bool use_customized_syscalls = true;
    int fd = use_customized_syscalls ? my_openat(AT_FDCWD, MAPS_FILE, O_RDONLY | O_CLOEXEC, 0)
                                     : openat(AT_FDCWD, MAPS_FILE, O_RDONLY | O_CLOEXEC, 0);
    if (fd == -1) {
        __android_log_print(ANDROID_LOG_INFO, TAG, "openat error %s : %d", strerror(errno), errno);
        return -1;
    }

    const int buf_size = 512;
    char buf[buf_size];
    char path[256];
    char perm[5];
    const char *sig = env->GetStringUTFChars(signature, nullptr);
    size_t sig_len = strlen(sig);

    uint64_t base, end, offset;
    jboolean result = JNI_FALSE;

    while ((read_line(fd, buf, buf_size, use_customized_syscalls)) > 0) {
        if (sscanf(buf, "%llx-%llx %4s %llx %*s %*s %s", &base, &end, perm, &offset, path) != 5) {
            continue;
        }

        if (perm[0] != 'r') continue;
        if (perm[3] != 'p') continue; //do not touch the shared memory
        if (0 != offset) continue;
        if (strlen(path) == 0) continue;
        if ('[' == path[0]) continue;
        if (end - base <= 1000000) continue;
        if (wrap_endsWith(path, ".oat")) continue;
        if (elf_check_header(base) != 1) continue;

        if (find_mem_string(base, end, (unsigned char *) sig, sig_len) == 1) {
            __android_log_print(ANDROID_LOG_INFO, TAG,
                                "frida signature \"%s\" found in %lx - %lx", sig, base, end);
            result = JNI_TRUE;
            break;
        }
    }

    return result;
}




extern "C"
JNIEXPORT jstring JNICALL
Java_com_mmg_phonect_device_utils_DebugUtils_checkFrida(JNIEnv *env, jclass clazz) {

    if (findFridaMaps()){
        return env->NewStringUTF("maps找到frida-agent");
    }
    //"frida:rpc"
//    unsigned char frida_rpc[] =
//            {
//
//                    0xfe, 0xba, 0xfb, 0x4a, 0x9a, 0xca, 0x7f, 0xfb,
//                    0xdb, 0xea, 0xfe, 0xdc
//            };
//
//    for (unsigned char &m : frida_rpc) {
//        unsigned char c = m;
//        c = ~c;
//        c ^= 0xb1;
//        c = (c >> 0x6) | (c << 0x2);
//        c ^= 0x4a;
//        c = (c >> 0x6) | (c << 0x2);
//        m = c;
//    }
    jstring frida_rpc = env->NewStringUTF("frida:rpc");
    if (check_fridaRpc(env,frida_rpc)){

        return env->NewStringUTF("找到frida:rpc");
    }



    //没啥效果  暂时不用了
//    jstring signature = env->NewStringUTF("LIBFRIDA");
////    const char *sig = ;
//    if (check_fridaRpc(env, signature)){
//
//        return env->NewStringUTF("找到LIBFRIDA");
//    }

    return env->NewStringUTF("");

}



//void install_check_arch_seccomp() {
//    struct sock_filter filter[15] = {
//            BPF_STMT(BPF_LD + BPF_W + BPF_ABS, (uint32_t) offsetof(struct seccomp_data, nr)),
//            BPF_JUMP(BPF_JMP + BPF_JEQ, __NR_getpid, 0, 12),
//            BPF_STMT(BPF_LD + BPF_W + BPF_ABS, (uint32_t) offsetof(struct seccomp_data, args[0])),
//            BPF_JUMP(BPF_JMP + BPF_JEQ, DetectX86Flag, 0, 10),
//            BPF_STMT(BPF_LD + BPF_W + BPF_ABS, (uint32_t) offsetof(struct seccomp_data, arch)),
//            BPF_JUMP(BPF_JMP + BPF_JEQ, AUDIT_ARCH_X86_64, 0, 1),
//            BPF_STMT(BPF_RET + BPF_K, SECCOMP_RET_ERRNO | (864 & SECCOMP_RET_DATA)),
//            BPF_JUMP(BPF_JMP + BPF_JEQ, AUDIT_ARCH_I386, 0, 1),
//            BPF_STMT(BPF_RET + BPF_K, SECCOMP_RET_ERRNO | (386 & SECCOMP_RET_DATA)),
//            BPF_JUMP(BPF_JMP + BPF_JEQ, AUDIT_ARCH_ARM, 0, 1),
//            BPF_STMT(BPF_RET + BPF_K, SECCOMP_RET_ERRNO | (0xA32 & SECCOMP_RET_DATA)),
//            BPF_JUMP(BPF_JMP + BPF_JEQ, AUDIT_ARCH_AARCH64, 0, 1),
//            BPF_STMT(BPF_RET + BPF_K, SECCOMP_RET_ERRNO | (0xA64 & SECCOMP_RET_DATA)),
//            BPF_STMT(BPF_RET + BPF_K, SECCOMP_RET_ERRNO | (6 & SECCOMP_RET_DATA)),
//            BPF_STMT(BPF_RET + BPF_K, SECCOMP_RET_ALLOW)
//
//    };
//    struct sock_fprog program = {
//            .len = (unsigned short) (sizeof(filter) / sizeof(filter[0])),
//            .filter = filter
//    };
//    errno = 0;
//    if (prctl(PR_SET_NO_NEW_PRIVS, 1, 0, 0, 0)) {
//        LOG(ERROR) << "prctl(PR_SET_NO_NEW_PRIVS) " << strerror(errno);
//    }
//    errno = 0;
//    if (prctl(PR_SET_SECCOMP, SECCOMP_MODE_FILTER, &program)) {
//        LOG(ERROR) << "prctl(PR_SET_SECCOMP) " << strerror(errno);
//    }
//}
//string check_arch_by_seccomp() {
//    if (get_sdk_level() < __ANDROID_API_N_MR1__){
//        return "";
//    }
//    errno = 0;
//    syscall(__NR_getpid, DetectX86Flag);
//    if (errno == 386) {
//        return "I386设备";
//    } else if (errno == 864) {
//        return "X86_64设备";
//    } else if (errno == 0xA32 || errno == 0xA64) {
//        return "";
//    }else if (errno == 0) {
//        //可能是没有开启seccomp
//        return "";
//    }
//    return ("疑似X86模拟器设备"+ to_string(errno));
//}