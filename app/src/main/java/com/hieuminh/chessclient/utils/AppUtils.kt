package com.hieuminh.chessclient.utils

import com.hieuminh.chessclient.common.constants.UrlConstants

object AppUtils {
    fun getServerUrl(): String {
        return "http://${UrlConstants.ANDROID_EMULATOR_LOCALHOST}:${UrlConstants.SERVER_PORT}"
    }

    fun getPath(username: String): String {
        return username.replace("\\s+".toRegex(), "")
    }
}
