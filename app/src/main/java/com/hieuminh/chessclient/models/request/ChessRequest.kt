package com.hieuminh.chessclient.models.request

import com.hieuminh.chessclient.models.Box
import com.hieuminh.chessclient.views.adapters.BoxAdapter
import java.io.Serializable

class ChessRequest : Serializable {
    var from: Box? = null

    var to: Box? = null

    var playerName: String? = null

    var rivalPlayerName: String? = null

    var roomId: Long? = null

    var isMoveSuggestionsOn: Boolean = false

    var level: Int? = null

    var checkmate: Boolean = false

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
