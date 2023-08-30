package com.mmg.phonect.settings.preference

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.mmg.phonect.common.ui.widgets.getCardListItemMarginDp
import com.mmg.phonect.settings.preference.composables.SectionFooter
import com.mmg.phonect.settings.preference.composables.SectionHeader

fun LazyListScope.sectionHeaderItem(
    @StringRes sectionTitleId: Int
) {
    val token = PreferenceToken.SectionHeader(sectionTitleId = sectionTitleId)
    item(
        key = token.preferenceKey,
        contentType = token::class.java,
    ) {
        SectionHeader(title = stringResource(sectionTitleId))
    }
}

fun LazyListScope.sectionFooterItem(
    @StringRes sectionTitleId: Int
) {
    val token = PreferenceToken.SectionFooter(sectionTitleId = sectionTitleId)
    item(
        key = token.preferenceKey,
        contentType = token::class.java,
    ) {
        SectionFooter()
    }
}

fun LazyListScope.bottomInsetItem() {
    val token = PreferenceToken.BottomInset()
    item(
        key = token.preferenceKey,
        contentType = token::class.java,
    ) {
        Column {
            Spacer(modifier = Modifier.height(getCardListItemMarginDp(LocalContext.current).dp))
            Spacer(modifier = Modifier.windowInsetsBottomHeight(WindowInsets.navigationBars))
        }
    }
}

fun LazyListScope.clickablePreferenceItem(
    @StringRes titleId: Int,
    content: @Composable (Int) -> Unit,
) {
    val token = PreferenceToken.ClickablePreference(titleId = titleId)
    item(
        key = token.preferenceKey,
        contentType = token::class.java,
    ) {
        content(titleId)
    }
}

fun LazyListScope.checkboxPreferenceItem(
    @StringRes titleId: Int,
    content: @Composable (Int) -> Unit,
) {
    val token = PreferenceToken.CheckboxPreference(titleId = titleId)
    item(
        key = token.preferenceKey,
        contentType = token::class.java,
    ) {
        content(titleId)
    }
}

fun LazyListScope.listPreferenceItem(
    @StringRes titleId: Int,
    content: @Composable (Int) -> Unit,
) {
    val token = PreferenceToken.ListPreference(titleId = titleId)
    item(
        key = token.preferenceKey,
        contentType = token::class.java,
    ) {
        content(titleId)
    }
}

fun LazyListScope.timePickerPreferenceItem(
    @StringRes titleId: Int,
    content: @Composable (Int) -> Unit,
) {
    val token = PreferenceToken.TimePickerPreference(titleId = titleId)
    item(
        key = token.preferenceKey,
        contentType = token::class.java,
    ) {
        content(titleId)
    }
}

fun LazyListScope.editTextPreferenceItem(
    @StringRes titleId: Int,
    content: @Composable (Int) -> Unit,
) {
    val token = PreferenceToken.EditTextPreference(titleId = titleId)
    item(
        key = token.preferenceKey,
        contentType = token::class.java,
    ) {
        content(titleId)
    }
}