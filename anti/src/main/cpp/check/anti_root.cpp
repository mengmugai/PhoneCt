//
// Created by 萌木盖 on 2023/10/5.
//

#include "anti_root.h"
#include "Utils.h"
#include "../JNIHelper/JNIHelper.hpp"

int check_system_property(){
    char value[5] = {0};
    char value1[5] = {0};
    __system_property_get("ro.secure",value);
    LOGD("ro.secure : %s",value);
    __system_property_get("ro.debuggable",value1);
    LOGD("ro.debuggable : %s:",value);
    if(value[0] == 0x30 || value1[0] == 0x31)
        return 1;
    else
        return 0;

}

int check_su_files(){
    FILE *fp,*fp1,*fp2,*fp3,*fp4,*fp5,*fp6,*fp7;
    fp = fopen("/sbin/su","r");
    fp1= fopen("/system/bin/su","r");
    fp2= fopen("/system/xbin/su","r");
    fp3= fopen("/data/local/xbin/su","r");
    fp4= fopen("/data/local/bin/su","r");
    fp5= fopen("/system/sd/xbin/su","r");
    fp6= fopen("/system/bin/failsafe/su","r");
    fp7= fopen("/data/local/su","r");
    if(fp || fp1 || fp2 || fp3 || fp4 || fp5 || fp6 || fp7){
        LOGD("su files has been found");
        return  1;
    } else{
        fp = fopen("/data/adb/magisk","r");
        fp1= fopen("/data/adb/magisk.db","r");
        fp2= fopen("/sbin/.magisk","r");
        fp3= fopen("/cache/.disable_magisk","r");
        fp4= fopen("/dev/.magisk.unblock","r");
        if (fp || fp1 || fp2 || fp3 || fp4 ){
            LOGD("su files has been found2");
            return  1;
        }
        LOGD("su files has not been found");
        return 0;
    }

}

int AntiRoot::get_root_status() {
    int ret = check_su_files();
    if(ret || check_system_property())
        return 1;
    else
        return 0;
}
