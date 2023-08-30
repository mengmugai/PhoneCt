package com.mmg.phonect.common.ui.widgets.insets

import android.content.Context
import android.os.Build
import android.util.AttributeSet
import android.view.WindowInsets
import androidx.annotation.RequiresApi
import androidx.core.view.ViewCompat
import com.google.android.material.appbar.AppBarLayout
import com.mmg.phonect.R
import com.mmg.phonect.common.basic.insets.FitBothSideBarHelper
import com.mmg.phonect.common.basic.insets.FitBothSideBarView
import com.mmg.phonect.common.basic.insets.FitBothSideBarView.FitSide
import com.mmg.phonect.common.utils.DisplayUtils
import com.mmg.phonect.theme.ThemeManager

class FitSystemBarAppBarLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : AppBarLayout(context, attrs, defStyleAttr),
    FitBothSideBarView {

    private val mHelper: FitBothSideBarHelper

    init {
        ViewCompat.setOnApplyWindowInsetsListener(this, null)
        mHelper = FitBothSideBarHelper(this, FitBothSideBarView.SIDE_TOP)
    }

    fun injectDefaultSurfaceTintColor() {
        setBackgroundColor(
            DisplayUtils.getWidgetSurfaceColor(
                6f,
                ThemeManager.getInstance(context).getThemeColor(context, R.attr.colorPrimary),
                ThemeManager.getInstance(context).getThemeColor(context, R.attr.colorSurface)
            )
        )
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT_WATCH)
    override fun onApplyWindowInsets(insets: WindowInsets): WindowInsets {
        return mHelper.onApplyWindowInsets(insets) { fitSystemBar() }
    }

    private fun fitSystemBar() {
        setPadding(0, mHelper.top(), 0, 0)
    }

    override fun addFitSide(@FitSide side: Int) {
        // do nothing.
    }

    override fun removeFitSide(@FitSide side: Int) {
        // do nothing.
    }

    override fun setFitSystemBarEnabled(top: Boolean, bottom: Boolean) {
        mHelper.setFitSystemBarEnabled(top, bottom)
    }

    override fun getTopWindowInset(): Int {
        return mHelper.top()
    }

    override fun getBottomWindowInset(): Int {
        return 0
    }
}