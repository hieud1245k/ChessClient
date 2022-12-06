package com.hieuminh.chessclient.models

import java.io.Serializable

class Player : Serializable {
    var id: Long = 0

    var name: String = ""

    var isActive: Boolean = false
}
