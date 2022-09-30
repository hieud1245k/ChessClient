package com.hieuminh.chessclient.common.enums

import com.hieuminh.chessclient.models.*

enum class ChessManType(val newInstance: () -> ChessMan) {
    PAWN({ Pawn() }),
    CASTLE({ Castle() }),
    KNIGHT({ Knight() }),
    BISHOP({ Bishop() }),
    QUEEN({ Queen() }),
    KING({ King() });

    companion object {
        fun getMainMap(): Map<Int, ChessManType> {
            return listOf(CASTLE, KNIGHT, BISHOP, QUEEN, KING, BISHOP, KNIGHT, CASTLE).mapIndexed { index, chessManType ->
                Pair(index, chessManType)
            }.toMap()
        }

        fun getGeneralPromotionList(): List<ChessManType> {
            return listOf(CASTLE, KNIGHT, BISHOP, QUEEN)
        }
    }
}
