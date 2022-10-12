package com.hieuminh.chessclient.views.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import com.hieuminh.chessclient.R
import com.hieuminh.chessclient.databinding.ItemRoomBinding
import com.hieuminh.chessclient.models.Room
import com.hieuminh.chessclient.views.adapters.base.BaseAdapter

class RoomAdapter : BaseAdapter<Room>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        return RoomViewHolder(ItemRoomBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    inner class RoomViewHolder(private val binding: ItemRoomBinding) : BaseViewHolder(binding.root) {
        override fun bind(data: Room) {
            binding.tvRoomId.text = data.roomTextId
            binding.ivPlayer1.setColorFilter(if (data.playerFirstName.isNullOrEmpty()) R.color.black else R.color.colorOrange)
            binding.ivPlayer2.setColorFilter(if (data.playerFirstName.isNullOrEmpty()) R.color.black else R.color.colorOrange)
        }
    }
}
