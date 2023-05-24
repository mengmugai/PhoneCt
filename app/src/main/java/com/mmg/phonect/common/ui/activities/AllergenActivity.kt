package wangdaye.com.geometricweather.common.ui.activities

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Text
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import wangdaye.com.geometricweather.R
import wangdaye.com.geometricweather.common.basic.GeoActivity
import wangdaye.com.geometricweather.common.basic.models.options.unit.PollenUnit
import wangdaye.com.geometricweather.common.basic.models.weather.Pollen
import wangdaye.com.geometricweather.common.ui.widgets.Material3CardListItem
import wangdaye.com.geometricweather.common.ui.widgets.Material3Scaffold
import wangdaye.com.geometricweather.common.ui.widgets.generateCollapsedScrollBehavior
import wangdaye.com.geometricweather.common.ui.widgets.getCardListItemMarginDp
import wangdaye.com.geometricweather.common.ui.widgets.insets.FitStatusBarTopAppBar
import wangdaye.com.geometricweather.common.ui.widgets.insets.bottomInsetItem
import wangdaye.com.geometricweather.db.DatabaseHelper
import wangdaye.com.geometricweather.theme.compose.DayNightTheme
import wangdaye.com.geometricweather.theme.compose.GeometricWeatherTheme

class AllergenActivity : GeoActivity() {

    companion object {
        const val KEY_ALLERGEN_ACTIVITY_LOCATION_FORMATTED_ID =
            "ALLERGEN_ACTIVITY_LOCATION_FORMATTED_ID"
    }

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
        val formattedId = intent.getStringExtra(KEY_ALLERGEN_ACTIVITY_LOCATION_FORMATTED_ID) ?: ""
        var location = DatabaseHelper.getInstance(this).readLocation(formattedId)
            ?: DatabaseHelper.getInstance(this).readLocationList()[0]

        location = location.copy(
            weather = DatabaseHelper.getInstance(this).readWeather(location)
        )
        val weather = location.weather
        if (weather == null) {
            finish()
            return
        }

        val unit = PollenUnit.PPCM

        val scrollBehavior = generateCollapsedScrollBehavior()

        Material3Scaffold(
            modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
            topBar = {
                FitStatusBarTopAppBar(
                    title = stringResource(R.string.allergen),
                    onBackPressed = { finish() },
                    scrollBehavior = scrollBehavior,
                )
            },
        ) {
            LazyColumn(modifier = Modifier.fillMaxHeight()) {
                items(weather.dailyForecast) { daily ->
                    val pollen = daily.pollen

                    Material3CardListItem {
                        Column {
                            Text(
                                modifier = Modifier.padding(dimensionResource(R.dimen.normal_margin)),
                                text = daily.getDate(stringResource(R.string.date_format_widget_long)),
                                color = DayNightTheme.colors.titleColor,
                                fontWeight = FontWeight.Bold,
                                style = MaterialTheme.typography.titleMedium,
                            )
                            Row {
                                PollenItem(
                                    modifier = Modifier
                                        .width(0.dp)
                                        .weight(1f),
                                    title = stringResource(R.string.grass),
                                    subtitle = unit.getValueText(
                                        this@AllergenActivity,
                                        pollen.grassIndex ?: 0
                                    ) + " - " + pollen.grassDescription,
                                    tintColor = Color(
                                        Pollen.getPollenColor(
                                            this@AllergenActivity,
                                            pollen.grassLevel
                                        )
                                    )
                                )
                                PollenItem(
                                    modifier = Modifier
                                        .width(0.dp)
                                        .weight(1f),
                                    title = stringResource(R.string.ragweed),
                                    subtitle = unit.getValueText(
                                        this@AllergenActivity,
                                        pollen.ragweedIndex ?: 0
                                    ) + " - " + pollen.ragweedDescription,
                                    tintColor = Color(
                                        Pollen.getPollenColor(
                                            this@AllergenActivity,
                                            pollen.ragweedLevel
                                        )
                                    )
                                )
                            }
                            Row {
                                PollenItem(
                                    modifier = Modifier
                                        .width(0.dp)
                                        .weight(1f),
                                    title = stringResource(R.string.tree),
                                    subtitle = unit.getValueText(
                                        this@AllergenActivity,
                                        pollen.treeIndex ?: 0
                                    ) + " - " + pollen.treeDescription,
                                    tintColor = Color(
                                        Pollen.getPollenColor(
                                            this@AllergenActivity,
                                            pollen.treeLevel
                                        )
                                    )
                                )
                                PollenItem(
                                    modifier = Modifier
                                        .width(0.dp)
                                        .weight(1f),
                                    title = stringResource(R.string.mold),
                                    subtitle = unit.getValueText(
                                        this@AllergenActivity,
                                        pollen.moldIndex ?: 0
                                    ) + " - " + pollen.moldDescription,
                                    tintColor = Color(
                                        Pollen.getPollenColor(
                                            this@AllergenActivity,
                                            pollen.moldLevel
                                        )
                                    )
                                )
                            }
                        }
                    }
                }

                bottomInsetItem(
                    extraHeight = getCardListItemMarginDp(this@AllergenActivity).dp
                )
            }
        }
    }

    @Composable
    private fun PollenItem(
        modifier: Modifier,
        title: String,
        subtitle: String,
        tintColor: Color,
    ) = Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            modifier = Modifier
                .padding(dimensionResource(R.dimen.normal_margin))
                .size(dimensionResource(R.dimen.material_icon_size)),
            painter = painterResource(R.drawable.ic_circle_medium),
            contentDescription = null,
            tint = tintColor,
        )
        Column(
            Modifier.padding(
                end = dimensionResource(R.dimen.normal_margin),
                top = dimensionResource(R.dimen.normal_margin),
                bottom = dimensionResource(R.dimen.normal_margin),
            )
        ) {
            Text(
                text = title,
                color = DayNightTheme.colors.titleColor,
                style = MaterialTheme.typography.titleMedium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
            Text(
                text = subtitle,
                color = DayNightTheme.colors.bodyColor,
                style = MaterialTheme.typography.bodyMedium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
        }
    }
}