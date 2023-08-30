package com.mmg.phonect.main.widgets

import android.content.Context
import android.util.AttributeSet
import com.mmg.phonect.common.ui.widgets.insets.FitSystemBarRecyclerView
import com.mmg.phonect.common.utils.DisplayUtils

class FitTabletRecyclerView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
): FitSystemBarRecyclerView(
    context, attrs, defStyleAttr
) {

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        val viewWidth = measuredWidth
        val adaptiveWidth = DisplayUtils.getTabletListAdaptiveWidth(context, viewWidth)
        val paddingHorizontal = (viewWidth - adaptiveWidth) / 2
        setPadding(paddingHorizontal, paddingTop, paddingHorizontal, paddingBottom)
    }
}