package com.hieuminh.chessclient.views.activities.base

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding
import com.hieuminh.chessclient.interfaces.InitLayout

abstract class BaseActivity<VBinding : ViewBinding> : AppCompatActivity(), InitLayout<VBinding> {
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
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
    }
}
