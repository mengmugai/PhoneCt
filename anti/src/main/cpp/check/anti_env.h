//
// Created by 萌木盖 on 2023/10/22.
//

#ifndef PHONECT_ANTI_ENV_H
#define PHONECT_ANTI_ENV_H

#include <string>

class AntiEnv {
public:
    std::string check();

private:
    bool check_file(std::string file_name);

};


#endif //PHONECT_ANTI_ENV_H
