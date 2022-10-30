package com.hieuminh.chessclient.common.extensions

import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.navigation.NavController
import androidx.navigation.NavDirections
import androidx.navigation.Navigation

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
        }
    }

    fun View.hideKeyboard() {
        clearFocus()
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager ?: return
        imm.hideSoftInputFromWindow(windowToken, 0)
    }
}
