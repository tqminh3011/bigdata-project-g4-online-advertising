package com.bigdata.group4demoapp.utils

import android.view.View
import androidx.viewpager.widget.ViewPager
import kotlin.math.abs

class CubeOutAnimation : ViewPager.PageTransformer {

    override fun transformPage(page: View, position: Float) {
        when {
            position < -1 -> {
                // This page is way off-screen to the left.
                page.alpha = ALPHA_0

            }
            position <= 0 -> {
                page.alpha = ALPHA_1
                page.pivotX = page.width.toFloat()
                page.rotationY = -ROTATION_SKEW_IN_PERCENT * abs(position)

            }
            position <= 1 -> {
                page.alpha = ALPHA_1
                page.pivotX = PIVOT_X
                page.rotationY = ROTATION_SKEW_IN_PERCENT * abs(position)
            }
            else -> {
                page.alpha = ALPHA_0
            }
        }
    }

    companion object {
        const val PIVOT_X = 0.0f
        const val ALPHA_0 = 0.0f
        const val ALPHA_1 = 1.0f
        const val ROTATION_SKEW_IN_PERCENT = 20
    }

}