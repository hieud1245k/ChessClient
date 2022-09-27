package com.hieuminh.chessclient.views.adapters

import android.view.LayoutInflater
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
            val bgColorResId = when {
                data.canKill -> R.color.colorLightRed
                data.canMove -> R.color.colorLightYellow
                data.isClicked -> R.color.colorLightGreen
                else -> if (data.x.plus(data.y).rem(2) == 1) R.color.white else R.color.colorWeighGreen
            }
            binding.flContainer.layoutParams = LinearLayout.LayoutParams(size, size)
            binding.flContainer.setBackgroundResource(bgColorResId)
            val chessMan = data.chessMan
            if (chessMan != null) {
                binding.ivImage.setImageResource(chessMan.imageResId)
                binding.ivImage.setColorFilter(itemView.context.getColor(chessMan.playerType.colorResId))
            } else {
                binding.ivImage.setImageDrawable(null)
            }
            binding.ivImage.setPadding(size / 5)
            binding.tvName.text = data.getPositionString()
        }
    }
}
