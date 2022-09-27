package com.hieuminh.chessclient.models

import com.hieuminh.chessclient.views.adapters.BoxAdapter

data class Box(
    val x: Int = 0,
    val y: Int = 0,
    var isClicked: Boolean = false,
    var canMove: Boolean = false,
    var canKill: Boolean = false,
    var chessMan: ChessMan? = null,
) {
    private fun getAdapterPosition(): Int {
        return y * 8 + x
    }

    fun getPositionString(): String {
        return "$x $y"
    }

    fun notifyChanged(adapter: BoxAdapter) {
        adapter.notifyItemChanged(getAdapterPosition())
    }
}
