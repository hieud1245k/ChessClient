package com.hieuminh.chessclient.views.fragments.inputname

import android.widget.Toast
import androidx.core.view.isVisible
import androidx.core.widget.doAfterTextChanged
import com.hieuminh.chessclient.common.constants.UrlConstants
import com.hieuminh.chessclient.common.extensions.ViewExtensions.navController
import com.hieuminh.chessclient.databinding.FragmentInputNameBinding
import com.hieuminh.chessclient.utils.AppUtils
import com.hieuminh.chessclient.views.fragments.base.BaseFragment
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

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
            baseActivity?.subscribe {
                stompClient.topic("/queue/add-username/${AppUtils.getPath(username)}")
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe {
                        val payLoad = it.payload
                        when (payLoad) {
                            username -> {
                                try {
                                    val action = InputNameFragmentDirections.actionInputNameFragmentToHomeFragment(username)
                                    view?.navController?.navigate(action)
                                } catch (e: Exception) {
                                    e.printStackTrace()
                                }
                            }
                            "404" -> {
                                binding.tvNameError.isVisible = true
                            }
                        }
                    }
            }

            baseActivity?.subscribe {
                stompClient.send("/app/add-username", username).compose(applySchedulers()).subscribe {
                    Toast.makeText(context, "Send add username success!", Toast.LENGTH_LONG).show()
                }
            }
        }
    }
}
