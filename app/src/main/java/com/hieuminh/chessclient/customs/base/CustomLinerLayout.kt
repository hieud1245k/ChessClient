package com.hieuminh.chessclient.customs.base

import android.content.Context
import android.util.AttributeSet
import android.widget.LinearLayout
import androidx.viewbinding.ViewBinding
import com.hieuminh.chessclient.interfaces.InitLayout

abstract class CustomLinerLayout<VBinding : ViewBinding> : LinearLayout, InitLayout<VBinding> {
    protected lateinit var binding: VBinding
        private set

    constructor(context: Context?) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }

    private fun init() {
        binding = getViewBinding()
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        initListener()
        initView()
    }
}
