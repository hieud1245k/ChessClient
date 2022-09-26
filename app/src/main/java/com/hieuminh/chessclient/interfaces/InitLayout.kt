package com.hieuminh.chessclient.interfaces

interface InitLayout<VBinding> {
    fun getViewBinding(): VBinding
    fun initListener()
    fun initView()
}
