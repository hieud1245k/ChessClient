package com.hieuminh.chessclient.views.fragments.inputname

import androidx.core.view.isVisible
import androidx.core.widget.doAfterTextChanged
import com.hieuminh.chessclient.common.constants.UrlConstants
import com.hieuminh.chessclient.common.extensions.ViewExtensions.navController
import com.hieuminh.chessclient.databinding.FragmentInputNameBinding
import com.hieuminh.chessclient.exceptions.CustomException
import com.hieuminh.chessclient.views.fragments.base.BaseFragment

open class InputNameFragment : BaseFragment<FragmentInputNameBinding>() {

    override fun getViewBinding() = FragmentInputNameBinding.inflate(layoutInflater)

    override fun initListener() {
        binding.btSubmit.setOnClickListener { submit() }
        binding.etInputName.doAfterTextChanged {
            binding.tvNameError.isVisible = false
        }
    }

    override fun initView() {
        binding.etIpAddress.setText(UrlConstants.ANDROID_EMULATOR_LOCALHOST)
        binding.etPort.setText(UrlConstants.SERVER_PORT)
    }

    private val username: String
        get() = binding.etInputName.text.toString().trim()

    private fun submit() {
        val ipAddress = binding.etIpAddress.text.toString().trim()
        val port = binding.etPort.text.toString().trim()

        baseActivity?.connect(ipAddress, port) { stompClient ->
            chessViewModel?.saveName(username, {
                val action = InputNameFragmentDirections.actionInputNameFragmentToHomeFragment(username)
                view?.navController?.navigate(action)
            }, {
                binding.tvNameError.isVisible = true
            })
        }
    }
}
