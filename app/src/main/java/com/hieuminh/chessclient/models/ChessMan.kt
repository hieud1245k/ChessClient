package com.hieuminh.chessclient.models

import com.hieuminh.chessclient.common.enums.PlayerType

abstract class ChessMan(val imageResId: Int) {
    var playerType: PlayerType = PlayerType.PLAYER_FIRST
}
