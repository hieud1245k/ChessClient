package com.hieuminh.chessclient.utils

import android.content.Context
import android.os.Build
import android.text.Html
import android.text.Spanned
import android.util.Log
import android.view.Gravity
import android.view.Menu
import android.view.View
import androidx.appcompat.widget.PopupMenu
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.hieuminh.chessclient.common.constants.UrlConstants
import com.hieuminh.chessclient.interfaces.PopupMenuListener
import java.lang.reflect.Field
import java.lang.reflect.Method

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

    fun showPopupMenu(activity: Context?, view: View?, menuRes: Int, listener: PopupMenuListener?, initPopupView: ((Menu) -> Unit)? = null) {
        if (null == activity || null == view) {
            return
        }
        val popup = PopupMenu(activity, view)
        popup.setOnMenuItemClickListener { menu ->
            listener?.itemClick(menu)
            return@setOnMenuItemClickListener true
        }
        popup.inflate(menuRes)
        popup.gravity = Gravity.END
        initPopupView?.invoke(popup.menu)
        setForceShowIcon(popup)
        popup.show()
    }

    private fun setForceShowIcon(popupMenu: PopupMenu) {
        try {
            val fields: Array<Field> = popupMenu.javaClass.declaredFields
            for (field in fields) {
                if ("mPopup" == field.name) {
                    field.isAccessible = true
                    val menuPopupHelper: Any? = field.get(popupMenu)
                    menuPopupHelper?.let {
                        val classPopupHelper = Class.forName(
                            menuPopupHelper
                                .javaClass.name
                        )
                        val setForceIcons: Method = classPopupHelper.getMethod(
                            "setForceShowIcon", Boolean::class.javaPrimitiveType
                        )
                        setForceIcons.invoke(menuPopupHelper, true)
                    }
                    break
                }
            }
        } catch (e: Throwable) {
            Log.d("ERROR", e.message?:"")
        }
    }

    fun fromHtml(html: String): Spanned {
        val url = decodeBOM(html)
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Html.fromHtml(url, Html.FROM_HTML_MODE_LEGACY)
        } else Html.fromHtml(url)
    }

    fun decodeBOM(url: String): String {
        return url.replace("\uFEFF", "")
    }
}
