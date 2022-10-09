package com.hieuminh.chessclient.services

import com.hieuminh.chessclient.models.Room
import kotlinx.coroutines.Deferred
import org.json.JSONObject
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface ChessService {
    @POST("player-name")
    fun savePlayerNameAsync(@Query("name") name: String): Deferred<JSONObject>

    @GET("api/rooms/")
    fun getRoomListAsync(): Deferred<List<Room>>

    @POST("api/rooms/")
    fun createNewRoomAsync(): Deferred<Room>
}
