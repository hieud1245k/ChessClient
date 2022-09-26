package com.hieuminh.chessclient.views.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.core.view.setPadding
import com.hieuminh.chessclient.R
import com.hieuminh.chessclient.databinding.ItemBoxBinding
import com.hieuminh.chessclient.models.Box
import com.hieuminh.chessclient.views.adapters.base.BaseAdapter

class BoxAdapter(private val size: Int) : BaseAdapter<Box>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        return BoxViewHolder(ItemBoxBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    inner class BoxViewHolder(private val binding: ItemBoxBinding) : BaseViewHolder(binding.root) {
        override fun bind(data: Box) {
            binding.flContainer.run {
                setBackgroundResource(if (data.x.plus(data.y).rem(2) == 1) R.color.white else R.color.colorWeighGreen)
                layoutParams = LinearLayout.LayoutParams(size, size)
            }
            data.chessMan?.let { chessMan ->
                binding.ivImage.setImageResource(chessMan.imageResId)
                binding.ivImage.setColorFilter(itemView.context.getColor(chessMan.playerType.colorResId))
            }
            binding.ivImage.setPadding(size / 5)
            binding.tvName.text = "${data.x} ${data.y}"
        }
    }
}
