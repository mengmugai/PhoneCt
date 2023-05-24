package com.mmg.phonect

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import dagger.hilt.android.AndroidEntryPoint
import wangdaye.com.geometricweather.common.basic.GeoActivity

@AndroidEntryPoint
class MainActivity : GeoActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}