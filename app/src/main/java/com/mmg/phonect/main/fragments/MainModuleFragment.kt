package com.mmg.phonect.main.fragments

import com.mmg.phonect.common.basic.GeoFragment
import com.mmg.phonect.common.bus.EventBus

class ModifyMainSystemBarMessage

abstract class MainModuleFragment: GeoFragment() {

    protected fun checkToSetSystemBarStyle() {
        EventBus
            .instance
            .with(ModifyMainSystemBarMessage::class.java)
            .postValue(ModifyMainSystemBarMessage())
    }

    abstract fun setSystemBarStyle()
}