package com.hieuminh.chessclient.views.fragments.mode

import android.os.Bundle
import com.hieuminh.chessclient.common.extensions.ViewExtensions.navigate
import com.hieuminh.chessclient.databinding.FragmentGameModeBinding
import com.hieuminh.chessclient.views.fragments.base.BaseFragment

class GameModeFragment : BaseFragment<FragmentGameModeBinding>() {
    private lateinit var name: String

    override fun getViewBinding() = FragmentGameModeBinding.inflate(layoutInflater)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val args = GameModeFragmentArgs.fromBundle(requireArguments())
        name = args.name
    }

    override fun initListener() {
        binding.tvOffline.setOnClickListener {
            view?.navigate(GameModeFragmentDirections.actionGameModeFragmentToOfllineChessFragment(name))
        }
        binding.tvOnline.setOnClickListener {
            view?.navigate(GameModeFragmentDirections.actionGameModeFragmentToHomeFragment(name))
        }
    }

    override fun initView() {
    }
}
