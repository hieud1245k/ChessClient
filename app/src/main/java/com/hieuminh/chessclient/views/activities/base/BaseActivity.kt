package com.hieuminh.chessclient.views.activities.base

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding
import com.hieuminh.chessclient.interfaces.InitLayout
import java.util.*

abstract class BaseActivity<VBinding : ViewBinding> : AppCompatActivity(), InitLayout<VBinding> {
    private var firstBackClicked: Boolean = false
    private var currentToast: Toast? = null

    lateinit var binding: VBinding
        private set

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = getViewBinding()
        setContentView(binding.root)
        initView()
        initListener()
    }

    protected open fun backToDestination() {
    }

    fun toast(text: String) {
        currentToast?.cancel()
        currentToast = Toast.makeText(this, text, Toast.LENGTH_SHORT)
        currentToast?.show()
    }

    override fun onBackPressed() {
        if (!firstBackClicked) {
            firstBackClicked = true
            toast("please click back again to exit!")
            Timer().schedule(object : TimerTask() {
                override fun run() {
                    firstBackClicked = false
                }
            }, 5000)
            return
        }
        finish()
    }
}
