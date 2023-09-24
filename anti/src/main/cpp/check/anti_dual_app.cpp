//
// Created by tg on 2022/9/4.
//

#include <fcntl.h>

#include "anti_dual_app.h"
#include "../JNIHelper/JNIHelper.hpp"
#include "../mini_io/_mini_io.h"
#include "Utils.h"
#include "../JNIHelper/app/ActivityThread.h"

std::string AntiDualApp::check() {

    return check_dual_app();
}

// https://bbs.pediy.com/thread-255212.htm
// 360分身大师测有问题
std::string AntiDualApp::check_dual_app() {
    jh::JNIEnvironmentGuarantee jniEnvironmentGuarantee;

    std::string self_fd = "/proc/self/fd";
    std::string simple_name = "wtf_jack";
    std::string data_dir_path = jh::jstringToStdString(Application().getDataDir());
    if (data_dir_path.empty()) {
        return "security";
    }
    std::string separator = "/";

    data_dir_path = data_dir_path + separator + simple_name;

    int flag = O_RDWR | O_CREAT | O_TRUNC;
    int fd = __openat(AT_FDCWD, data_dir_path.c_str(), flag, 0666);
    LOGE("data_dir_path: %s", data_dir_path.c_str());

    if (-1 == fd) {
        LOGE("-1 == fd");
        return "security";
    } else {
        char fd_path[80] = {0};
        sprintf(fd_path, "%s%d", (self_fd + separator).c_str(), fd);

        LOGE("fd_path: %s", fd_path);

        char buff[1024] = {0};

        std::size_t len = _readlinkat(AT_FDCWD, fd_path, buff, sizeof(buff));

        if (len < 0) {
            LOGE("len < 0");
            return "security";
        }

        buff[len] = '\0';
        int count_file = 0;
        LOGE("buff's len : %d", strlen(buff));
        LOGE("buff: %s", buff);

        for (int i = strlen(buff); i > 0; i--) {
            if (buff[i] != '/') {
                buff[i] = '\0';
            } else {
                if (count_file == 0) {
                    count_file++;
                } else {
                    if (access(buff, R_OK) == 0) {

                        return "checked";
                    }
                }
            }
        }
    }

    return "security";
}
