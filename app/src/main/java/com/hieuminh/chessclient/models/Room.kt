package com.hieuminh.chessclient.models

import android.os.Parcel
import android.os.Parcelable

class Room() : Parcelable {
    var id: Long? = null

    var playerFirstName: String? = null

    var playerSecondName: String? = null

    val roomTextId: String
        get() = "Room id: $id"

    fun getRivalPlayerName(name: String?): String? {
        if (name.equals(playerFirstName)) {
            return playerSecondName
        }
        return playerFirstName
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
