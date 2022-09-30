package com.hieuminh.chessclient.views.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import com.hieuminh.chessclient.databinding.ItemGeneralPromotionBinding
import com.hieuminh.chessclient.models.ChessMan
import com.hieuminh.chessclient.views.adapters.base.BaseAdapter

class GeneralPromotionAdapter : BaseAdapter<ChessMan>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        return GeneralPromotionViewHolder(ItemGeneralPromotionBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    inner class GeneralPromotionViewHolder(private val binding: ItemGeneralPromotionBinding) : BaseViewHolder(binding.root) {
        override fun bind(data: ChessMan) {
            binding.ivGeneral.setImageResource(data.imageResId)
        }
    }
}
