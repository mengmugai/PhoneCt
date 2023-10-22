package com.mmg.phonect.device.utils

import android.content.Context
import java.io.BufferedReader
import java.io.FileInputStream
import java.io.InputStreamReader
import com.tg.android.anti.NativeLib
import com.tg.android.anti.NativeLib.AntiEnv

class AbnormalEnvironment {

    private fun checkProcSelfMaps(): Boolean {
        var susMapsString = false
        try {
            val ret = StringBuilder()
            val smapsFile = FileInputStream("/proc/self/smaps")
            val mapsFile = FileInputStream("/proc/self/maps")
            val files = listOf(smapsFile, mapsFile)
            for (f in files) {
                lateinit var reader: InputStreamReader
                lateinit var bufReader: BufferedReader
                try {
                    reader = InputStreamReader(f, Charsets.UTF_8)
                    bufReader = BufferedReader(reader)

                    while (true) {
                        val line = bufReader.readLine() ?: break
                        val l = line.split("/", "_", "-").toString()
                        if (l.contains(".magisk", ignoreCase = true)
                            || (l.contains("riru", ignoreCase = true)
                                    || (l.contains("zygisk", ignoreCase = true)))
                        ) {
                            ret.append(l).append('\n')
                        }
                    }
                } catch (e: Exception) {
                    // do nothing
                } finally {
                    bufReader.close()
                    reader.close()
                }

                susMapsString = ret.isNotEmpty()
            }
            smapsFile.close()
            mapsFile.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return susMapsString
    }



    fun detectDual(context:Context): String {
        if (!NativeLib.AntiDualApp().equals("security")){
            return "存在多开文件夹"

        }
        val filedir = context.filesDir.path
        return if (filedir.startsWith("/data/user") && !filedir.startsWith("/data/user/0"))
            "在多开空间内工作"
        else ""
    }

//    private fun detectFile(path: String): String {
//        var res = FileDetection.detect(path, true)
//        if (res == Result.METHOD_UNAVAILABLE) res = FileDetection.detect(path, false)
//        if (res == Result.FOUND) res = Result.SUSPICIOUS
//        return res
//    }

    fun checkEnv(context: Context): String {

        val susSelfMaps = checkProcSelfMaps()
        if (susSelfMaps){
            return "进程中存在Magisk/Riru/Zygisk"
        }
        if(HookUtils.riruCheck()||HookUtils.riru2Check(context)){
            return "存在Riru相关痕迹"
        }
        if (AntiEnv().equals("checked")){
            return "存在框架常用模块相关痕迹"
        }
//        if
//        add("Dual / Work profile" to detectDual(context))
//        add(Pair("HMA (old version)", detectFile("/data/misc/hide_my_applist")))
//        add("XPrivacyLua" to detectFile("/data/system/xlua"))
//        add(
//            "TWRP" to if (detectFile("/storage/emulated/0/TWRP") != Result.NOT_FOUND
//                || detectFile("/storage/emulated/TWRP") != Result.NOT_FOUND
//            )
//                Result.SUSPICIOUS else Result.NOT_FOUND
//        )
//        add(Pair("Xposed Edge", detectFile("/data/system/xedge")))
//        add(Pair("Riru Clipboard", detectFile("/data/misc/clipboard")))
//        add(Pair("隐秘空间", detectFile("/data/system/cn.geektang.privacyspace")))
//        add(
//            Pair(
//                "Magisk/Riru/Zygisk Maps Scan",
//                if (susSelfMaps) Result.FOUND else Result.NOT_FOUND
//            )
//        )
        return ""
    }


}