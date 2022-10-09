package com.hieuminh.chessclient.models

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

class Room() : Parcelable {
    var id: Long? = null
    var playerFirstSessionId: String? = null
    var playerSecondSessionId: String? = null

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
