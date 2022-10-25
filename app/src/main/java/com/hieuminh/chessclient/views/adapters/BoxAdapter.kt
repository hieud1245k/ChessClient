package com.hieuminh.chessclient.views.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.setPadding
import com.hieuminh.chessclient.R
import com.hieuminh.chessclient.common.extensions.ContextExtensions.isTablet
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
                data.canKill -> R.drawable.bg_can_kill
                data.canMove -> R.drawable.bg_can_move
                data.isClicked -> R.color.colorLightGreen
                else -> if (data.x.plus(data.y).rem(2) == 1) R.color.white else R.color.colorWeighGreen
            }
            binding.llEntryContainer.layoutParams = ViewGroup.LayoutParams(size, size)
            binding.llBoxContainer.setBackgroundResource(bgColorResId)
            val chessMan = data.chessMan
            if (chessMan != null) {
                binding.ivImage.setImageResource(chessMan.imageResId)
                binding.ivImage.setColorFilter(itemView.context.getColor(chessMan.playerType.colorResId))
            } else {
                binding.ivImage.setImageDrawable(null)
            }
            binding.ivImage.setPadding(size / if (itemView.context.isTablet) 8 else 12)

            if (data.justJump) {
                binding.llEntryContainer.setBackgroundResource(R.color.colorOrange)
                binding.llBoxContainer.setBackgroundResource(bgColorResId)
            } else {
                binding.llEntryContainer.setBackgroundResource(bgColorResId)
                binding.llBoxContainer.background = null
            }
        }
    }
}
