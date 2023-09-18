package com.mmg.phonect.settings.activities

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Text
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mmg.phonect.BuildConfig
import com.mmg.phonect.R
import com.mmg.phonect.common.basic.GeoActivity
import com.mmg.phonect.common.ui.widgets.Material3CardListItem
import com.mmg.phonect.common.ui.widgets.Material3Scaffold
import com.mmg.phonect.common.ui.widgets.generateCollapsedScrollBehavior
import com.mmg.phonect.common.ui.widgets.getCardListItemMarginDp
import com.mmg.phonect.common.ui.widgets.insets.FitStatusBarTopAppBar
import com.mmg.phonect.common.ui.widgets.insets.bottomInsetItem
import com.mmg.phonect.common.utils.helpers.IntentHelper
import com.mmg.phonect.theme.compose.DayNightTheme
import com.mmg.phonect.theme.compose.GeometricWeatherTheme
import com.mmg.phonect.theme.compose.rememberThemeRipple

private class AboutAppLinkItem(
    @DrawableRes val iconId: Int,
    @StringRes val titleId: Int,
    val onClick: () -> Unit,
)

private class ContributorItem(
    val name: String,
    val url: String,
    val flag: String,
)

class AboutActivity : GeoActivity() {

    private val aboutAppLinks = arrayOf(1
//        AboutAppLinkItem(
//            iconId = R.drawable.ic_github,
//            titleId = R.string.gitHub,
//        ) {
//            IntentHelper.startWebViewActivity(
//                this@AboutActivity,
//                "https://github.com/WangDaYeeeeee/GeometricWeather"
//            )
//        },
//        AboutAppLinkItem(
//            iconId = R.drawable.ic_email,
//            titleId = R.string.email,
//        ) {
//            IntentHelper.startWebViewActivity(
//                this@AboutActivity,
//                "mailto:wangdayeeeeee@gmail.com"
//            )
//        },
    )
    private val donateLinks = arrayOf(2
//        AboutAppLinkItem(
//            iconId = R.drawable.ic_alipay,
//            titleId = R.string.alipay,
//        ) {
//            DonateHelper.donateByAlipay(this)
//        },
//        AboutAppLinkItem(
//            iconId = R.drawable.ic_wechat_pay,
//            titleId = R.string.wechat,
//        ) {
//            DonateHelper.donateByWechat(this)
//        },
    )
    private val contributors = arrayOf(
        ContributorItem(
            name = "mengmugai",
            url = "https://github.com/mengmugai",
            flag = "🇨🇳",
        ),

    )
    private val translators = arrayOf(
        ContributorItem(
            name = "mengmugai",
            url = "mengmugai",
            flag = "🇨🇳",
        ),

    )

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
                    title = stringResource(R.string.action_about),
                    onBackPressed = { finish() },
                    scrollBehavior = scrollBehavior,
                )
            },
        ) {
            LazyColumn(
                modifier = Modifier.fillMaxHeight(),
            ) {
                item {
                    Header()
                    SectionTitle(stringResource(R.string.about_app))
                }
//                items(aboutAppLinks) { item ->
//                    AboutAppLink(
//                        iconId = item.iconId,
//                        title = stringResource(item.titleId),
//                        onClick = item.onClick,
//                    )
//                }
//
//                item { SectionTitle(stringResource(R.string.donate)) }
//                items(donateLinks) { item ->
//                    AboutAppLink(
//                        iconId = item.iconId,
//                        title = stringResource(item.titleId),
//                        onClick = item.onClick,
//                    )
//                }

                item { SectionTitle(stringResource(R.string.contributor)) }
                items(contributors) { item ->
                    Translator(name = item.name, url = item.url, flag = item.flag)
                }

                item { SectionTitle(stringResource(R.string.translator)) }
                items(translators) { item ->
                    Translator(name = item.name, url = item.url, flag = item.flag)
                }

                bottomInsetItem(
                    extraHeight = getCardListItemMarginDp(this@AboutActivity).dp
                )
            }
        }
    }

    @Composable
    private fun Header() {
        Column(
            modifier = Modifier.padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
//            Image(
//                painter = painterResource(R.drawable.ic_launcher_round),
//                contentDescription = null,
//                modifier = Modifier.size(72.dp),
//            )
            Spacer(
                modifier = Modifier
                    .height(dimensionResource(R.dimen.little_margin))
                    .fillMaxWidth()
            )
            Text(
                text = stringResource(R.string.phone_ct),
                color = DayNightTheme.colors.titleColor,
                style = MaterialTheme.typography.headlineSmall,
            )
            Text(
                text = BuildConfig.VERSION_NAME,
                color = DayNightTheme.colors.captionColor,
                style = MaterialTheme.typography.labelMedium,
            )
        }
    }

    @Composable
    private fun SectionTitle(title: String) {
        Text(
            text = title,
            modifier = Modifier.padding(dimensionResource(R.dimen.normal_margin)),
            color = DayNightTheme.colors.captionColor,
            style = MaterialTheme.typography.labelMedium,
        )
    }

    @Composable
    private fun AboutAppLink(
        @DrawableRes iconId: Int,
        title: String,
        onClick: () -> Unit,
    ) {
        Material3CardListItem {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = rememberThemeRipple(),
                        onClick = onClick,
                    )
                    .padding(dimensionResource(R.dimen.normal_margin)),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Icon(
                    painter = painterResource(iconId),
                    contentDescription = null,
                    tint = DayNightTheme.colors.titleColor,
                )
                Spacer(modifier = Modifier.width(dimensionResource(R.dimen.normal_margin)))
                Text(
                    text = title,
                    color = DayNightTheme.colors.titleColor,
                    style = MaterialTheme.typography.titleMedium,
                )
            }
        }
    }

    @Composable
    private fun Translator(name: String, url: String, flag: String) {
        Material3CardListItem {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = rememberThemeRipple(),
                        onClick = {
                            IntentHelper.startWebViewActivity(this@AboutActivity, url)
                        },
                    )
                    .padding(dimensionResource(R.dimen.normal_margin)),
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = name,
                        color = DayNightTheme.colors.titleColor,
                        style = MaterialTheme.typography.titleMedium,
                    )
                    Spacer(modifier = Modifier.width(dimensionResource(R.dimen.little_margin)))
                    Text(
                        text = flag,
                        style = MaterialTheme.typography.titleMedium,
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