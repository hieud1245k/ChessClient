package com.hieuminh.chessclient.models

import android.os.Parcel
import android.os.Parcelable
import com.hieuminh.chessclient.databinding.LayoutStartGameBinding

class Room() : Parcelable {
    var id: Long? = null

    var playerFirstName: String? = null

    var playerSecondName: String? = null

    var firstPlay: String? = null

    var isOnline: Boolean = true

    val roomTextId: String
        get() = "Room id: $id"

    fun getRivalPlayerName(name: String?): String? {
        if (name.equals(playerFirstName)) {
            return playerSecondName
        }
        return playerFirstName
    }

    fun resetPlayerName(name: String) {
        if (playerFirstName == name) {
            playerFirstName = null
        } else {
            playerSecondName = null
        }
    }

    fun isFullPlayer(): Boolean {
        return playerFirstName != null && playerSecondName != null
    }

    fun setPlayerInfo(startGameBinding: LayoutStartGameBinding) {
        startGameBinding.run {
            tvRoomId.text = "Room id: $id"
            tvPlayerFirstName.text = playerFirstName
            tvPlayerSecondName.text = playerSecondName
        }
    }

    constructor(parcel: Parcel) : this()

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel?, flags: Int) = Unit

    companion object CREATOR : Parcelable.Creator<Room> {
        override fun createFromParcel(parcel: Parcel): Room {
            return Room(parcel)
        }

        override fun newArray(size: Int): Array<Room?> {
            return arrayOfNulls(size)
        }
    }
}
