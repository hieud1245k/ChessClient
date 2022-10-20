package com.hieuminh.chessclient.views.fragments.inputname

import android.util.Log
import com.hieuminh.chessclient.common.constants.UrlConstants
import com.hieuminh.chessclient.common.extensions.ViewExtensions.navController
import com.hieuminh.chessclient.databinding.FragmentInputNameBinding
import com.hieuminh.chessclient.views.fragments.base.BaseFragment

open class InputNameFragment : BaseFragment<FragmentInputNameBinding>() {

    override fun getViewBinding() = FragmentInputNameBinding.inflate(layoutInflater)

    override fun initListener() {
        binding.btSubmit.setOnClickListener { submit() }
    }

    override fun initView() {
        binding.etIpAddress.setText(UrlConstants.ANDROID_EMULATOR_LOCALHOST)
        binding.etPort.setText(UrlConstants.SERVER_PORT)
    }

    private fun submit() {
        val ipAddress = binding.etIpAddress.text.toString().trim()
        val port = binding.etPort.text.toString().trim()
        val username = binding.etInputName.text.toString().trim()

        baseActivity?.connect(ipAddress, port) { stompClient ->
//            baseActivity?.subscribe(stompClient.topic("/users/queue/add-username")
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe {
//                    val newUsername = it.payload
//                    val action = InputNameFragmentDirections.actionInputNameFragmentToHomeFragment(newUsername)
//                    view?.navController?.navigate(action)
//                })
            baseActivity?.subscribe(
                stompClient.send("/app/add-username", username)
                    .compose(applySchedulers())
                    .subscribe({
                        val action = InputNameFragmentDirections.actionInputNameFragmentToHomeFragment(username)
                        view?.navController?.navigate(action)
                    }, {
                        Log.e(this.tag, "Error send STOMP echo")
                    })
            )
        }
    }
}
