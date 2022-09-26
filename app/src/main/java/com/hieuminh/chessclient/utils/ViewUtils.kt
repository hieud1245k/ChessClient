package com.hieuminh.chessclient.utils

import android.app.Activity
import android.graphics.Insets
import android.os.Build
import android.util.DisplayMetrics
import android.view.WindowInsets

object ViewUtils {
    fun getScreenWidth(activity: Activity?): Int {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            val windowMetrics = activity?.windowManager?.currentWindowMetrics
            val insets: Insets? = windowMetrics?.windowInsets?.getInsetsIgnoringVisibility(WindowInsets.Type.systemBars())
            return (windowMetrics?.bounds?.width() ?: 0) - (insets?.left ?: 0) - (insets?.right ?: 0)
        }
        val displayMetrics = DisplayMetrics()
        activity?.windowManager?.defaultDisplay?.getMetrics(displayMetrics)
        return displayMetrics.widthPixels
    }

    fun getScreenHeight(activity: Activity?): Int {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            val windowMetrics = activity?.windowManager?.currentWindowMetrics
            val insets: Insets? = windowMetrics?.windowInsets?.getInsetsIgnoringVisibility(WindowInsets.Type.systemBars())
            return (windowMetrics?.bounds?.height() ?: 0) - (insets?.top ?: 0) - (insets?.bottom ?: 0)
        }
        val displayMetrics = DisplayMetrics()
        activity?.windowManager?.defaultDisplay?.getMetrics(displayMetrics)
        return displayMetrics.heightPixels
    }
}
