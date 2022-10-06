package com.hieuminh.chessclient.views.fragments.inputname

import androidx.core.widget.addTextChangedListener
import androidx.navigation.Navigation
import com.hieuminh.chessclient.databinding.FragmentInputNameBinding
import com.hieuminh.chessclient.views.fragments.base.BaseFragment

class InputNameFragment : BaseFragment<FragmentInputNameBinding>() {

    override fun getViewBinding() = FragmentInputNameBinding.inflate(layoutInflater)

    override fun initListener() {
        binding.etInputName.addTextChangedListener { onTextChangedListener(it?.toString()) }
        binding.btSubmit.setOnClickListener { submit() }
    }

    override fun initView() {

    }

    private fun onTextChangedListener(text: String?) {

    }

    private fun submit() {
        val navController = Navigation.findNavController(binding.root)
        navController.navigate(InputNameFragmentDirections.actionInputNameFragmentToHomeFragment())
    }
}
