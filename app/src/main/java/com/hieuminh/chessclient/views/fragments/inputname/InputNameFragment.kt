package com.hieuminh.chessclient.views.fragments.inputname

import android.util.Log
import androidx.core.view.isVisible
import androidx.core.widget.doAfterTextChanged
import com.hieuminh.chessclient.R
import com.hieuminh.chessclient.common.constants.UrlConstants
import com.hieuminh.chessclient.common.extensions.ViewExtensions.navController
import com.hieuminh.chessclient.databinding.FragmentInputNameBinding
import com.hieuminh.chessclient.utils.AppUtils
import com.hieuminh.chessclient.utils.AppUtils.enable
import com.hieuminh.chessclient.views.fragments.base.BaseFragment
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

open class InputNameFragment : BaseFragment<FragmentInputNameBinding>() {
    private var isFirstCheck = true

    private val username: String
        get() = binding.etInputName.text.toString().trim()

    override fun getViewBinding() = FragmentInputNameBinding.inflate(layoutInflater)

    private fun updateSubmitButtonState() {
        val isSubmitEnable = username.length in 6..30 && !username.contains("\\s+".toRegex())
        binding.btSubmit.enable(isSubmitEnable)
        binding.tvNameError.setText(R.string.name_must_be_not_contain_blank_and_between_6_and_30_characters)
        binding.tvNameError.isVisible = !isSubmitEnable && !isFirstCheck
        isFirstCheck = false
    }

    override fun initListener() {
        binding.btSubmit.setOnClickListener { submit() }
        binding.etInputName.doAfterTextChanged {
            updateSubmitButtonState()
        }
    }

    override fun initView() {
        binding.etIpAddress.setText(UrlConstants.ANDROID_EMULATOR_LOCALHOST)
        binding.etPort.setText(UrlConstants.SERVER_PORT)
        updateSubmitButtonState()
    }

    private fun submit() {
        val ipAddress = binding.etIpAddress.text.toString().trim()
        val port = binding.etPort.text.toString().trim()

        UrlConstants.run {
            ANDROID_EMULATOR_LOCALHOST = ipAddress
            SERVER_PORT = port
        }

        chessActivity?.connect(ipAddress, port) { stompClient ->
            subscribe {
                stompClient.topic("/queue/add-username/${AppUtils.getPath(username)}")
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe {
                        val payLoad = it.payload
                        when (payLoad) {
                            username -> {
                                try {
                                    val action = InputNameFragmentDirections.actionInputNameFragmentToGameModeFragment(username)
                                    view?.navController?.navigate(action)
                                } catch (e: Exception) {
                                    e.printStackTrace()
                                }
                            }
                            "404" -> {
                                binding.tvNameError.isVisible = true
                                binding.tvNameError.setText(R.string.name_is_exist_please_input_another_name)
                            }
                        }
                    }
            }

            subscribe {
                stompClient.send("/app/add-username", username).compose(applySchedulers()).subscribe {
                    Log.d("ADD_USERNAME", "Send add username success!")
                }
            }
        }
    }
}
