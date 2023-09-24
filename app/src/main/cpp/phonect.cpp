#include <jni.h>

#include "include/utils.h"
#include "include/log.h"
#include "include/emulator-check.h"
#include "include/virtual-apk-check.h"
#include "include/debug-check.h"

//extern "C"
//JNIEXPORT jint JNICALL
//Java_com_mmg_phonect_decive_utils_EmulatorUtils_bluetoothCheck(JNIEnv *env, jclass thiz) {
//    return bluetoothCheck();
//}
//
//extern "C"
//JNIEXPORT jint JNICALL
//Java_com_mmg_phonect_decive_utils_EmulatorUtils_getArch(JNIEnv *env, jclass thiz) {
//    return getArch();
//}
//
//extern "C"
//JNIEXPORT jstring JNICALL
//Java_com_mmg_phonect_decive_utils_EmulatorUtils_getMapsArch(JNIEnv *env, jclass thiz) {
//    char dst[BUF_SIZE_64] = UNKNOWN;
//    getMapsArch(dst);
//    return env->NewStringUTF(dst);
//}
//
//extern "C"
//JNIEXPORT jint JNICALL
//Java_com_mmg_phonect_decive_utils_EmulatorUtils_specialFilesEmulatorCheck(JNIEnv *env, jclass clazz) {
//    return specialFilesEmulatorcheck();
//}
//
//extern "C"
//JNIEXPORT jint JNICALL
//Java_com_mmg_phonect_decive_utils_EmulatorUtils_thermalCheck(JNIEnv *env, jclass clazz) {
//    return thermalCheck();
//}
//
//extern "C"
//JNIEXPORT jint JNICALL
//Java_com_mmg_detection_info_VirtualAppInfo_moreOpenCheck(JNIEnv *env, jclass clazz,
//                                                           jobject context) {
//    return moreOpenCheck(env, context);
//}


extern "C"
JNIEXPORT jint JNICALL
Java_com_mmg_phonect_device_utils_DebugUtils_getTracerPid(JNIEnv *env, jclass clazz) {
    return getTracerPid();
}

//int Abc(){
//
//    const char *path = getApkPath(env, context);
//    //check svc apk sign
//    const string &string = checkSign(env, path).substr(0, 10);
//    LOG(INFO) << "apk sign  " << string;
//    if (string == Base64Utils::VTDecode("TFtCRU58UERAUQ==")) {
//    //check sign success,but maybe svc io hook
//    //check apk path
//    int fd = my_openat(AT_FDCWD, reinterpret_cast<const char *>(path),
//                       O_RDONLY | O_CLOEXEC,
//                       0640);
//    //check apk path
//    char buff[PATH_MAX] = {0};
//    std::string fdPath("/proc/");
//    fdPath.append(to_string(getpid())).append("/fd/").append(to_string(fd));
//    long len = raw_syscall(__NR_readlinkat, AT_FDCWD, fdPath.c_str(), buff, PATH_MAX);
//    if (len < 0) {
//    return getItemData(env, "APK签名验证失败",
//    "readlinkat error", true,
//    RISK_LEAVE_DEADLY, TAG_REPACKAGE);
//    }
//    //截断,如果攻击者hook了readlinkat,只修改了参数,没修改返回值也可以检测出来。
//    buff[len] = '\0';
//    LOG(INFO) << "check apk sign path " << buff;
//    if (my_strcmp(path, buff) == 0) {
//    LOG(INFO) << "check apk sign path success ";
//    //start check memory&location inode
//    struct stat statBuff = {0};
//    long stat = raw_syscall(__NR_fstat, fd, &statBuff);
//    if (stat < 0) {
//    LOG(ERROR) << "check apk sign path fail __NR_fstat<0";
//    return getItemData(env, "APK签名验证失败",
//    "fstat error", true, RISK_LEAVE_DEADLY, TAG_REPACKAGE);
//    }
//    //check uid&gid (1000 = system group)
//    if (statBuff.st_uid != 1000 && statBuff.st_gid != 1000) {
//    LOG(ERROR) << "check apk sign gid&uid fail ";
//    return getItemData(env, "APK签名验证失败",
//    nullptr, true, RISK_LEAVE_DEADLY, TAG_REPACKAGE);
//    }
//    size_t inode = getFileInMapsInode(path);
//    if (statBuff.st_ino != inode) {
//    LOG(ERROR) << "check apk sign inode fail "<<statBuff.st_ino<<" maps ->"<<inode;
//    return getItemData(env, "APK签名验证失败",
//    nullptr, true, RISK_LEAVE_DEADLY, TAG_REPACKAGE);
//    }
//    LOG(ERROR) << ">>>>>>>>>> check apk sign success! uid-> " << statBuff.st_uid
//    << " gid-> "
//    << statBuff.st_gid;
//    } else {
//    LOG(ERROR) << "check apk sign path fail ";
//    return getItemData(env, "APK签名验证失败",
//    nullptr, true, RISK_LEAVE_DEADLY, TAG_REPACKAGE);
//
//    }
//    LOG(INFO) << "check apk sign success";
//
//    return nullptr;
//}