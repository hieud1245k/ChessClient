package com.hieuminh.chessclient.common.extensions

import android.content.Context
import android.content.res.Configuration
import com.hieuminh.chessclient.R

object ContextExtensions {
    val Context?.isTablet: Boolean
        get() = this?.resources?.getBoolean(R.bool.isTablet) ?: false

    val Context?.isLandscape: Boolean
        get() = this?.resources?.configuration?.orientation == Configuration.ORIENTATION_LANDSCAPE
}
