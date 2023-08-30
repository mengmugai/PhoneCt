package com.mmg.phonect.settings.preference.composables

import androidx.annotation.StringRes
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.mmg.phonect.R
import com.mmg.phonect.common.ui.widgets.Material3CardListItem
import com.mmg.phonect.common.ui.widgets.defaultCardListItemElevation
import com.mmg.phonect.theme.compose.DayNightTheme
import com.mmg.phonect.theme.compose.rememberThemeRipple

@Composable
fun PreferenceView(
    @StringRes titleId: Int,
    @StringRes summaryId: Int,
    enabled: Boolean = true,
    onClick: () -> Unit,
) = PreferenceView(
    title = stringResource(titleId),
    summary = stringResource(summaryId),
    enabled = enabled,
    onClick = onClick
)

@Composable
fun PreferenceView(
    title: String,
    summary: String? = null,
    enabled: Boolean = true,
    onClick: () -> Unit,
) = Material3CardListItem(
    elevation = if (enabled) defaultCardListItemElevation else 0.dp
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .alpha(if (enabled) 1f else 0.5f)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = rememberThemeRipple(),
                onClick = onClick,
                enabled = enabled,
            )
            .padding(dimensionResource(R.dimen.normal_margin)),
    ) {
        Text(
            text = title,
            color = DayNightTheme.colors.titleColor,
            style = MaterialTheme.typography.titleMedium,
        )
        if (summary?.isNotEmpty() == true) {
            Spacer(modifier = Modifier.height(dimensionResource(R.dimen.little_margin)))
            Text(
                text = summary,
                color = DayNightTheme.colors.bodyColor,
                style = MaterialTheme.typography.bodyMedium,
            )
        }
    }
}