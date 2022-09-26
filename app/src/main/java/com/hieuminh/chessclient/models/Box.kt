package com.hieuminh.chessclient.models

data class Box(
    val x: Int = 0,
    val y: Int = 0,
    var chessMan: ChessMan? = null,
)
