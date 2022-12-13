package com.hieuminh.chessclient.models

import android.os.Parcel
import android.os.Parcelable

class Player() : Parcelable {
    var name: String = ""
    var level: Int = 1

    constructor(parcel: Parcel) : this() {
        name = parcel.readString().toString()
        level = parcel.readInt()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(name)
        parcel.writeInt(level)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Player> {
        override fun createFromParcel(parcel: Parcel): Player {
            return Player(parcel)
        }

        override fun newArray(size: Int): Array<Player?> {
            return arrayOfNulls(size)
        }
    }
}
