package com.hieuminh.chessclient.views.fragments.dialogs

import com.hieuminh.chessclient.common.enums.ChessManType
import com.hieuminh.chessclient.databinding.FragmentPawnPromotionBinding
import com.hieuminh.chessclient.models.ChessMan
import com.hieuminh.chessclient.views.adapters.GeneralPromotionAdapter
import com.hieuminh.chessclient.views.adapters.base.BaseAdapter
import com.hieuminh.chessclient.views.fragments.dialogs.base.BaseDialogFragment

class PawnPromotionFragment : BaseDialogFragment<FragmentPawnPromotionBinding>(), BaseAdapter.ItemEventListener<ChessMan> {
    private var itemClickedListener: BaseAdapter.ItemEventListener<ChessMan>? = null

    override fun getViewBinding() = FragmentPawnPromotionBinding.inflate(layoutInflater)

    override fun onItemClick(item: ChessMan, position: Int) {
        dismiss()
        itemClickedListener?.onItemClick(item, position)
    }

    override fun initListener() {
        binding.ivClose.setOnClickListener { dismiss() }
    }

    override fun initView() {
        binding.rvGeneralPromotionList.adapter = GeneralPromotionAdapter().apply {
            updateData(ChessManType.getGeneralPromotionList().map { it.newInstance.invoke() }.toMutableList())
            setItemListener(this@PawnPromotionFragment)
        }
    }

    fun setGeneralItemListener(itemClickedListener: BaseAdapter.ItemEventListener<ChessMan>?) {
        this.itemClickedListener = itemClickedListener
    }
}
