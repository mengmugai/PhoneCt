package com.mmg.phonect.common.ui.widgets.insets

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.material.AppBarDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.compositeOver
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.mmg.phonect.R
import com.mmg.phonect.common.ui.widgets.getWidgetSurfaceColor
import kotlin.math.ln

private val topAppBarElevation = 6.dp

internal fun ColorScheme.applyTonalElevation(backgroundColor: Color, elevation: Dp): Color {
    return if (backgroundColor == surface) {
        surfaceColorAtElevation(elevation)
    } else {
        backgroundColor
    }
}
internal fun ColorScheme.surfaceColorAtElevation(elevation: Dp): Color {
    if (elevation == 0.dp) return surface
    val alpha = ((4.5f * ln(elevation.value + 1)) + 2f) / 100f
    return surfaceTint.copy(alpha = alpha).compositeOver(surface)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FitStatusBarTopAppBar(
    title: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    navigationIcon: @Composable (() -> Unit) = {},
    actions: @Composable RowScope.() -> Unit = {},
    scrollBehavior: TopAppBarScrollBehavior? = null,
) = Column {
    Spacer(
        modifier = Modifier
            .background(getWidgetSurfaceColor(topAppBarElevation))
            .windowInsetsTopHeight(WindowInsets.statusBars)
            .fillMaxWidth(),
    )
    MediumTopAppBar(
        title = title,
        modifier = modifier,
        navigationIcon = navigationIcon,
        actions = actions,
        colors = TopAppBarDefaults.mediumTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.surface,
            scrolledContainerColor = MaterialTheme.colorScheme.applyTonalElevation(
                backgroundColor = MaterialTheme.colorScheme.surface,
                elevation = topAppBarElevation,
            ),
            navigationIconContentColor = MaterialTheme.colorScheme.onSurface,
            titleContentColor = MaterialTheme.colorScheme.onSurface,
            actionIconContentColor = MaterialTheme.colorScheme.onSurface,
        ),
        scrollBehavior = scrollBehavior,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FitStatusBarTopAppBar(
    title: String,
    onBackPressed: () -> Unit,
    actions: @Composable RowScope.() -> Unit = {},
    scrollBehavior: TopAppBarScrollBehavior? = null,
) = FitStatusBarTopAppBar(
    title = { Text(text = title) },
    navigationIcon = {
        IconButton(onClick = onBackPressed) {
            Icon(
                imageVector = Icons.Filled.ArrowBack,
                contentDescription = stringResource(R.string.content_desc_back),
                tint = MaterialTheme.colorScheme.onPrimaryContainer,
            )
        }
    },
    actions = actions,
    scrollBehavior = scrollBehavior,
)

@Composable
fun FitNavigationBarBottomAppBar(
    modifier: Modifier = Modifier,
    containerColor: Color = MaterialTheme.colorScheme.primaryContainer,
    contentColor: Color = contentColorFor(containerColor),
    tonalElevation: Dp = AppBarDefaults.BottomAppBarElevation,
    contentPadding: PaddingValues = BottomAppBarDefaults.ContentPadding,
    content: @Composable RowScope.() -> Unit
) {
    Box {
        Column {
            BottomAppBar(
                modifier = modifier,
                containerColor = containerColor,
                contentColor = contentColor,
                tonalElevation = tonalElevation,
                contentPadding = contentPadding,
                content = content,
            )
        }
        Spacer(
            modifier = Modifier
                .background(containerColor)
                .windowInsetsBottomHeight(WindowInsets.navigationBars)
                .fillMaxWidth()
                .align(Alignment.BottomCenter),
        )
    }
}

enum class BottomInsetKey { INSTANCE }
fun LazyListScope.bottomInsetItem(
    extraHeight: Dp = 0.dp,
) = item(
    key = { BottomInsetKey.INSTANCE },
    contentType = { BottomInsetKey.INSTANCE },
) {
    Column {
        Spacer(modifier = Modifier.height(extraHeight))
        Spacer(modifier = Modifier.windowInsetsBottomHeight(WindowInsets.navigationBars))
    }
}