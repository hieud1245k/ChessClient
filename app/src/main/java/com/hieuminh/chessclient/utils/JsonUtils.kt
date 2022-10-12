package com.hieuminh.chessclient.utils

import com.google.gson.Gson
import com.google.gson.JsonElement

object JsonUtils {
    inline fun <reified T> fromJson(stringJson: Any?): T? {
        if (null == stringJson) {
            return null
        }
        return try {
            when (stringJson) {
                is JsonElement -> {
                    Gson().fromJson(stringJson, T::class.java)
                }
                is String -> {
                    Gson().fromJson(stringJson, T::class.java)
                }
                else -> {
                    null
                }
            }

        } catch (e: java.lang.Exception) {
            e.printStackTrace()
            null
        }
    }

    fun toJson(any: Any?): String {
        if (null == any) {
            return ""
        }
        return try {
            Gson().toJson(any)
        } catch (e: java.lang.Exception) {
            ""
        }
    }
}
