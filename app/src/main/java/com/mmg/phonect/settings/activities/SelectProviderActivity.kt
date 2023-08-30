package com.mmg.phonect.settings.activities

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.Scaffold
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.mmg.phonect.R
import com.mmg.phonect.common.basic.GeoActivity
import com.mmg.phonect.common.ui.widgets.Material3Scaffold
import com.mmg.phonect.common.ui.widgets.generateCollapsedScrollBehavior
import com.mmg.phonect.common.ui.widgets.insets.FitStatusBarTopAppBar
import com.mmg.phonect.settings.compose.ServiceProviderSettingsScreen
import com.mmg.phonect.settings.compose.SettingsProviderAdvancedSettingsScreen
import com.mmg.phonect.settings.compose.SettingsScreenRouter
import com.mmg.phonect.theme.compose.GeometricWeatherTheme

class SelectProviderActivity : GeoActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            GeometricWeatherTheme(lightTheme = !isSystemInDarkTheme()) {
                ContentView()
            }
        }
    }

    @Composable
    private fun ContentView() {
        val scrollBehavior = generateCollapsedScrollBehavior()

        Material3Scaffold(
            modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
            topBar = {
                FitStatusBarTopAppBar(
                    title = stringResource(R.string.settings_title_service_provider),
                    onBackPressed = { finish() },
                    scrollBehavior = scrollBehavior,
                )
            },
        ) {
            val navController = rememberNavController()
            NavHost(
                navController = navController,
                startDestination = SettingsScreenRouter.ServiceProvider.route
            ) {
                composable(SettingsScreenRouter.ServiceProvider.route) {
                    ServiceProviderSettingsScreen(
                        context = this@SelectProviderActivity,
                        navController = navController
                    )
                }
                composable(SettingsScreenRouter.ServiceProviderAdvanced.route) {
                    SettingsProviderAdvancedSettingsScreen(
                        context = this@SelectProviderActivity
                    )
                }
            }
        }
    }

    @Preview
    @Composable
    private fun DefaultPreview() {
        GeometricWeatherTheme(lightTheme = isSystemInDarkTheme()) {
            ContentView()
        }
    }
}