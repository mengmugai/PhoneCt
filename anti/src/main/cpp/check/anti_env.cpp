//
// Created by 萌木盖 on 2023/10/22.
//

#include "anti_env.h"
#include <fcntl.h>
#include <string>
#include <vector>
#include "../utils/Utils.h"
#include "../mini_io/_open.c"


std::string AntiEnv::check() {
    std::string result = "security";
    std::vector<std::string> list_of_files =
            {
                    "/data/misc/hide_my_applist",
                    "/data/system/xlua",
                    "/storage/emulated/0/TWRP",
                    "/data/system/xedge",
                    "/data/misc/clipboard",
                    "/data/system/cn.geektang.privacyspace"
            };
    for (auto file: list_of_files) {
        if (check_file(file)) {
            result = "checked";
            break;
        }
    }

    LOGE("result: %s", result.c_str());
    return result;
}

bool AntiEnv::check_file(std::string file_name) {
    int fd = _access(file_name.c_str(), O_RDONLY);
    if (fd == errno || fd == -1) {
        return false;
    }
    return true;
}