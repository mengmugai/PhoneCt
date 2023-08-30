package com.mmg.phonect.settings.preference.composables

import android.content.Context
import android.widget.TimePicker
import androidx.annotation.StringRes
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.mmg.phonect.R
import com.mmg.phonect.common.ui.widgets.Material3CardListItem
import com.mmg.phonect.common.ui.widgets.defaultCardListItemElevation
import com.mmg.phonect.theme.compose.DayNightTheme
import com.mmg.phonect.theme.compose.rememberThemeRipple

@Composable
fun TimePickerPreferenceView(
    @StringRes titleId: Int,
    currentTime: String,
    enabled: Boolean = true,
    onValueChanged: (String) -> Unit,
) = TimePickerPreferenceView(
    title = stringResource(titleId),
    summary = { _, it -> it },
    currentTime = currentTime,
    enabled = enabled,
    onValueChanged = onValueChanged,
)

@Composable
fun TimePickerPreferenceView(
    title: String,
    summary: (Context, String) -> String?, // currentTime (xx:xx) -> summary.
    currentTime: String,
    enabled: Boolean = true,
    onValueChanged: (String) -> Unit,
) {

    val currentTimeState = remember { mutableStateOf(currentTime) }
    val dialogOpenState = remember { mutableStateOf(false) }

    Material3CardListItem(
        elevation = if (enabled) defaultCardListItemElevation else 0.dp
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .alpha(if (enabled) 1f else 0.5f)
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = rememberThemeRipple(),
                    onClick = { dialogOpenState.value = true },
                    enabled = enabled,
                )
                .padding(dimensionResource(R.dimen.normal_margin)),
            verticalArrangement = Arrangement.Center,
        ) {
            Column {
                Text(
                    text = title,
                    color = DayNightTheme.colors.titleColor,
                    style = MaterialTheme.typography.titleMedium,
                )
                val currentSummary = summary(LocalContext.current, currentTimeState.value)
                if (currentSummary?.isNotEmpty() == true) {
                    Spacer(modifier = Modifier.height(dimensionResource(R.dimen.little_margin)))
                    Text(
                        text = currentSummary,
                        color = DayNightTheme.colors.bodyColor,
                        style = MaterialTheme.typography.bodyMedium,
                    )
                }
            }
        }
    }

    if (dialogOpenState.value) {
        val timePickerState = remember { mutableStateOf(currentTimeState.value) }

        AlertDialog(
            onDismissRequest = { dialogOpenState.value = false },
            title = {
                Text(
                    text = title,
                    color = MaterialTheme.colorScheme.onSurface,
                    style = MaterialTheme.typography.headlineSmall,
                )
            },
            text = {
                AndroidView(
                    factory = { context ->
                        val timePicker = TimePicker(
                            context,
                            null,
                            R.style.Widget_Material3_MaterialTimePicker
                        )
                        timePicker.setIs24HourView(true)
                        timePicker.setOnTimeChangedListener { _, hour, minute ->
                            timePickerState.value = timeToString(hour = hour, minute = minute)
                        }

                        val time = stringToTime(currentTimeState.value)
                        timePicker.currentHour = time.elementAtOrNull(0) ?: 0
                        timePicker.currentMinute = time.elementAtOrNull(1) ?: 0
                        timePickerState.value = timeToString(
                            hour = timePicker.currentHour,
                            minute = timePicker.currentMinute
                        )

                        timePicker
                    }
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        currentTimeState.value = timePickerState.value
                        dialogOpenState.value = false
                        onValueChanged(currentTimeState.value)
                    }
                ) {
                    Text(
                        text = stringResource(R.string.done),
                        color = MaterialTheme.colorScheme.primary,
                        style = MaterialTheme.typography.labelLarge,
                    )
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { dialogOpenState.value = false }
                ) {
                    Text(
                        text = stringResource(R.string.cancel),
                        color = MaterialTheme.colorScheme.primary,
                        style = MaterialTheme.typography.labelLarge,
                    )
                }
            }
        )
    }
}

private fun timeToString(
    hour: Int,
    minute: Int
) = when {
    hour == 0 -> "00"
    hour < 10 -> "0$hour"
    else -> hour.toString()
} + ":" + when {
    minute == 0 -> "00"
    minute < 10 -> "0$minute"
    else -> minute.toString()
}

private fun stringToTime(
    time: String
) = time.split(":").map { it.toIntOrNull() ?: 0 }