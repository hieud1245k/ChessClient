package com.hieuminh.chessclient.common.extensions

import android.view.View
import androidx.navigation.NavController
import androidx.navigation.NavDirections
import androidx.navigation.Navigation
import com.hieuminh.chessclient.common.extensions.ViewExtensions.navController

object ViewExtensions {
    val View.navController: NavController?
        get() = try {
            Navigation.findNavController(this)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }

    fun View.navigate(directions: NavDirections) {
        try {
            navController?.navigate(directions)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}
