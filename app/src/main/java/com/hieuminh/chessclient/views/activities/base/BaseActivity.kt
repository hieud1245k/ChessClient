package com.hieuminh.chessclient.views.activities.base

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding
import com.hieuminh.chessclient.common.constants.UrlConstants
import com.hieuminh.chessclient.interfaces.InitLayout
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.WebSocketListener

abstract class BaseActivity<VBinding : ViewBinding> : AppCompatActivity(), InitLayout<VBinding> {
    protected lateinit var client: OkHttpClient

    lateinit var binding: VBinding
        private set

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = getViewBinding()
        setContentView(binding.root)
        client = OkHttpClient()
        initListener()
        initView()
    }

    fun openInputNameSocket(path: String, listener: WebSocketListener) {
        val request = Request.Builder().url("${UrlConstants.LOCAL_API_URL}/$path").build()
        client.newWebSocket(request, listener)
        client.dispatcher.executorService.shutdown()
    }
}
