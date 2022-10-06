package com.hieuminh.chessclient.common.extensions

import android.view.View
import androidx.navigation.NavController
import androidx.navigation.Navigation

object ViewExtensions {
    val View.navController: NavController?
        get() = try {
            Navigation.findNavController(this)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
}
