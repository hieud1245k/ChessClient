package com.hieuminh.chessclient.views.fragments.inputname

import androidx.core.widget.addTextChangedListener
import androidx.navigation.Navigation
import com.hieuminh.chessclient.databinding.FragmentInputNameBinding
import com.hieuminh.chessclient.views.activities.base.BaseActivity
import com.hieuminh.chessclient.views.fragments.base.BaseFragment
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import okio.ByteString

class InputNameFragment : BaseFragment<FragmentInputNameBinding>() {
    private lateinit var client: OkHttpClient

    override fun getViewBinding() = FragmentInputNameBinding.inflate(layoutInflater)

    override fun initListener() {
        binding.etInputName.addTextChangedListener { onTextChangedListener(it?.toString()) }
        binding.btSubmit.setOnClickListener { submit() }
    }

    override fun initView() {
        client = OkHttpClient()
    }

    private fun onTextChangedListener(text: String?) {

    }

    private fun submit() {
        (activity as? BaseActivity<*>)?.openSocket("name", webSocketListener)
    }

    private val webSocketListener = object : WebSocketListener() {
        override fun onOpen(webSocket: WebSocket, response: Response) {
            val text = binding.etInputName.text?.toString() ?: return
            if (text.isNotBlank()) {
                webSocket.send(text)
            }
        }

        override fun onMessage(webSocket: WebSocket, text: String) {
            if (binding.etInputName.text.toString() == text) {
                val navController = Navigation.findNavController(binding.root)
                navController.navigate(InputNameFragmentDirections.actionInputNameFragmentToHomeFragment(text))
            }
        }

        override fun onMessage(webSocket: WebSocket, bytes: ByteString) {
            super.onMessage(webSocket, bytes)
        }

        override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
            super.onClosed(webSocket, code, reason)
        }

        override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
            super.onClosing(webSocket, code, reason)
        }

        override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
            super.onFailure(webSocket, t, response)
        }
    }
}
