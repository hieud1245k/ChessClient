package com.hieuminh.chessclient.views.activities

import com.hieuminh.chessclient.R
import com.hieuminh.chessclient.common.extensions.ViewExtensions.navController
import com.hieuminh.chessclient.databinding.ActivityChessBinding
import com.hieuminh.chessclient.views.activities.base.BaseActivity

class ChessActivity : BaseActivity<ActivityChessBinding>() {
    override fun getViewBinding() = ActivityChessBinding.inflate(layoutInflater)

    override fun initListener() {
    }

    override fun initView() {
    }

    override fun backToDestination() {
        binding.navOnboardFragments.navController?.navigate(R.id.inputNameFragment)
    }
}
