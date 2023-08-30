package com.mmg.phonect.settings.activities

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.mmg.phonect.R
import com.mmg.phonect.common.basic.GeoActivity
import com.mmg.phonect.common.bus.EventBus
import com.mmg.phonect.common.ui.widgets.Material3Scaffold
import com.mmg.phonect.common.ui.widgets.generateCollapsedScrollBehavior
import com.mmg.phonect.common.ui.widgets.insets.FitStatusBarTopAppBar
import com.mmg.phonect.common.utils.helpers.IntentHelper
import com.mmg.phonect.settings.SettingsChangedMessage
import com.mmg.phonect.settings.SettingsManager
import com.mmg.phonect.settings.compose.*
import com.mmg.phonect.theme.compose.GeometricWeatherTheme

class SettingsActivity : GeoActivity() {

    private val cardDisplayState = mutableStateOf(
        SettingsManager.getInstance(this).cardDisplayList
    )
    private val dailyTrendDisplayState = mutableStateOf(
        SettingsManager.getInstance(this).dailyTrendDisplayList
    )
    private val hourlyTrendDisplayState = mutableStateOf(
        SettingsManager.getInstance(this).hourlyTrendDisplayList
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            GeometricWeatherTheme(lightTheme = !isSystemInDarkTheme()) {
                ContentView()
            }
        }

        EventBus.instance.with(SettingsChangedMessage::class.java).observe(this) {
            val cardDisplayList = SettingsManager.getInstance(this).cardDisplayList
            if (cardDisplayState.value != cardDisplayList) {
                cardDisplayState.value = cardDisplayList
            }

            val dailyTrendDisplayList = SettingsManager.getInstance(this).dailyTrendDisplayList
            if (dailyTrendDisplayState.value != dailyTrendDisplayList) {
                dailyTrendDisplayState.value = dailyTrendDisplayList
            }

            val hourlyTrendDisplayList = SettingsManager.getInstance(this).hourlyTrendDisplayList
            if (hourlyTrendDisplayState.value != hourlyTrendDisplayList) {
                hourlyTrendDisplayState.value = hourlyTrendDisplayList
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
                    title = stringResource(R.string.action_settings),
                    onBackPressed = { finish() },
                    actions = {
                        IconButton(
                            onClick = {
                                IntentHelper.startAboutActivity(this@SettingsActivity)
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.Info,
                                contentDescription = stringResource(R.string.action_about),
                                tint = MaterialTheme.colorScheme.onPrimaryContainer,
                            )
                        }
                    },
                    scrollBehavior = scrollBehavior,
                )
            },
        ) {
            val navController = rememberNavController()
            NavHost(
                navController = navController,
                startDestination = SettingsScreenRouter.Root.route
            ) {
                composable(SettingsScreenRouter.Root.route) {
                    RootSettingsView(
                        context = this@SettingsActivity,
                        navController = navController
                    )
                }
                composable(SettingsScreenRouter.Appearance.route) {
                    AppearanceSettingsScreen(
                        context = this@SettingsActivity,
                        cardDisplayList = remember { cardDisplayState }.value,
                        dailyTrendDisplayList = remember { dailyTrendDisplayState }.value,
                        hourlyTrendDisplayList = remember { hourlyTrendDisplayState }.value,
                    )
                }
                composable(SettingsScreenRouter.ServiceProvider.route) {
                    ServiceProviderSettingsScreen(
                        context = this@SettingsActivity,
                        navController = navController
                    )
                }
                composable(SettingsScreenRouter.ServiceProviderAdvanced.route) {
                    SettingsProviderAdvancedSettingsScreen(
                        context = this@SettingsActivity
                    )
                }
                composable(SettingsScreenRouter.Unit.route) {
                    UnitSettingsScreen(
                        context = this@SettingsActivity
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