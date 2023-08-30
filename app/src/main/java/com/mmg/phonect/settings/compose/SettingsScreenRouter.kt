package com.mmg.phonect.settings.compose

sealed class SettingsScreenRouter(val route: String) {
    object Root : SettingsScreenRouter("com.mmg.phonect.settings.root")
    object Appearance : SettingsScreenRouter("com.mmg.phonect.settings.appearance")
    object ServiceProvider : SettingsScreenRouter("com.mmg.phonect.settings.providers")
    object ServiceProviderAdvanced : SettingsScreenRouter("com.mmg.phonect.settings.advanced")
    object Unit : SettingsScreenRouter("com.mmg.phonect.settings.unit")
}