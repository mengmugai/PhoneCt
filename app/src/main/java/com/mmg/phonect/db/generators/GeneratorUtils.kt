package com.mmg.phonect.db.generators

object GeneratorUtils {

    @JvmStatic
    fun nonNull(string: String?): String {
        return string ?: ""
    }
}