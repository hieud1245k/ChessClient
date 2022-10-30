package com.hieuminh.chessclient.utils

import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import com.hieuminh.chessclient.common.constants.UrlConstants

object AppUtils {
    fun getServerUrl(): String {
        return "http://${UrlConstants.ANDROID_EMULATOR_LOCALHOST}:${UrlConstants.SERVER_PORT}"
    }

    fun getPath(username: String): String {
        return username.replace("\\s+".toRegex(), "")
    }
}
