//package com.mmg.phonect.device.info
//
//import android.annotation.SuppressLint
//import android.content.Context
//import android.content.Intent
//import android.content.pm.PackageManager
//import android.graphics.drawable.Drawable
//import android.os.Build
//import android.util.Log
//import com.mmg.phonect.common.basic.models.weather.XposedModule
//
//// 备选方案   先不用了
//
//
//
//class XposedModules {
//
//
//    companion object {
//
//
//        data class Setting(val meta: String, val meta2: String)
//        //override val name = "Xposed Modules"
//        @JvmStatic
//        @SuppressLint("QueryPermissions OR PMCAPermissions Needed")
//        fun getXposedAppListInfo(context: Context): List<XposedModule> {
//        //        if (packages != null) throw IllegalArgumentException("packages should be null")
//            Log.d("tag-applist","label+++++++++++++++++=")
//            val result = mutableListOf<XposedModule>()
//            val pm = context.packageManager
//        //        val set = if (detail == null) null else mutableSetOf<Pair<String, Result>>()
//            val intent = pm.getInstalledApplications(PackageManager.GET_META_DATA)
////            var meta="";var meta2=""
////            var lspatch = false
//            val settings = listOf(
//                Setting("lspatch", "jshook"),
//                Setting("xposedminversion", "xposeddescription")
//            )
//            for (setting in settings) {
//                val (meta, meta2) = setting
////            if (lspatch) {meta="lspatch";meta2="jshook"}else{meta="xposedminversion";meta2="xposeddescription"}
//                for (pkg in intent) {
//
//                    if (pkg.metaData?.get(meta) != null || pkg.metaData?.get(meta2) != null) {
//
//                        val bean = XposedModule().apply {
//                            name = pm.getApplicationLabel(pkg) as String
//                            packageName = pkg.packageName
//                            version = "versionName"
//                            //                    version =
//                            icon = pm.getApplicationIcon(pkg) as Drawable
//                            buildVersion = pkg.targetSdkVersion
//                        }
//                        //                result = Result.FOUND
//                        result.add(bean)
//                    }
//                }
//
//                if (result.isEmpty()){
//                val intent = pm.queryIntentActivities(Intent(Intent.ACTION_MAIN),PackageManager.GET_META_DATA)
//                for (pkg in intent) {
//
//                    val ainfo=pkg.activityInfo.applicationInfo
//                    Log.d("tag-applist",pm.getApplicationLabel(ainfo)as String)
//                    if (meta=="lspatch"){
//                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
//                            if (ainfo.appComponentFactory?.contains("lsposed") == true
//                            ){
//                                val bean = XposedModule().apply {
//                                    name = pm.getApplicationLabel(ainfo) as String
//                                    packageName = ainfo.packageName
//                                    version = "versionName"
//
//                                    icon = pm.getApplicationIcon(ainfo) as Drawable
//                                    buildVersion = ainfo.targetSdkVersion
//                                }
//                                result.add(bean)
//                            }
//                        }
//                    }
//                    if (ainfo.metaData?.get(meta) != null || ainfo.metaData?.get(meta2)!=null) {
//                        val bean = XposedModule().apply {
//                            name = pm.getApplicationLabel(ainfo) as String
//                            packageName = ainfo.packageName
//                            version = "versionName"
//
//                            icon = pm.getApplicationIcon(ainfo) as Drawable
//                            buildVersion = ainfo.targetSdkVersion
//                        }
//                        result.add(bean)
//                    }
//                }
//            }
//            }
//        //        detail?.addAll(set!!)
//            return result
//        }
//    }
//}