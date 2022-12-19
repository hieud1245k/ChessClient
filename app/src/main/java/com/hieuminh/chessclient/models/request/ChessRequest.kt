package com.hieuminh.chessclient.models.request

import com.hieuminh.chessclient.models.Box
import com.hieuminh.chessclient.views.adapters.BoxAdapter
import java.io.Serializable

class ChessRequest : Serializable {
    var from: Box? = null

    var to: Box? = null

    var playerName: String? = null

    var roomId: Long? = null

    var checkmate: Boolean = false

    var youWin: Boolean? = null

    fun resetJump(boxAdapter: BoxAdapter) {
        from?.run {
            justJump = false
            notifyChanged(boxAdapter)
        }

        to?.run {
            justJump = false
            notifyChanged(boxAdapter)
        }
    }
}
