package com.hieuminh.chessclient.common.extensions

object StringExtensions {
    fun String.toLongSafe(): Long {
        return try {
            this.toLong()
        } catch (e: Exception) {
            e.printStackTrace()
            0
        }
    }
}
