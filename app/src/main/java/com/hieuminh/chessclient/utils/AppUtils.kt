package com.hieuminh.chessclient.utils

import android.util.Log
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.hieuminh.chessclient.common.constants.UrlConstants

object AppUtils {
    fun getServerUrl(): String {
        return "http://${UrlConstants.ANDROID_EMULATOR_LOCALHOST}:${UrlConstants.SERVER_PORT}"
    }

    fun getPath(username: String): String {
        return username.replace("\\s+".toRegex(), "")
    }

    fun getCurrentFragment(activity: FragmentActivity?): Fragment? {
        try {
            return activity?.supportFragmentManager?.fragments?.lastOrNull { fragment -> fragment.isVisible && fragment.isAdded }
        } catch (e: Exception) {
            Log.d("GET_CURRENT_FRAGMENT_ERROR", "Have something wrong when show popup on tablet! cause: ${e.message}")
        }
        return null
    }

    fun DialogFragment.dismissSafe() {
        try {
            dismiss()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
