package com.hieuminh.chessclient.models.request

import com.hieuminh.chessclient.models.Box
import java.io.Serializable

class ChessRequest : Serializable {
    var from: Box? = null

    var to: Box? = null

    var playerName: String? = null
}
