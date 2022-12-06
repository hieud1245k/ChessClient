package com.hieuminh.chessclient.models

import android.os.Parcel
import android.os.Parcelable

class Room() : Parcelable {
    var id: Long = 0

    var playerFirst: Player? = null

    var playerSecond: Player? = null

    var firstPlay: Player? = null

    var isOnline: Boolean = true

    val roomTextId: String
        get() = "Room id: $id"

    fun getRivalPlayer(currentPlayerName: String?): Player? {
        if (currentPlayerName.equals(playerFirst?.name)) {
            return playerSecond
        }
        return playerFirst
    }

    fun resetPlayerName(currentPlayerName: String) {
        if (playerFirst?.name == currentPlayerName) {
            playerFirst = null
        } else {
            playerSecond = null
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
