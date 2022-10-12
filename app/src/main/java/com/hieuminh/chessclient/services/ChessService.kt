package com.hieuminh.chessclient.services

import com.hieuminh.chessclient.models.Room
import kotlinx.coroutines.Deferred
import org.json.JSONObject
import retrofit2.http.*

interface ChessService {
    @POST("player-name")
    fun savePlayerNameAsync(@Query("name") name: String): Deferred<JSONObject>

    @GET("api/rooms/")
    fun getRoomListAsync(): Deferred<List<Room>>

    @POST("api/rooms/")
    @FormUrlEncoded
    fun createNewRoomAsync(@Field("name") name: String): Deferred<Room>

    @PUT("api/rooms/{id}")
    @FormUrlEncoded
    fun joinRoomAsync(@Path("id") id: Long, @Field("name") name: String): Deferred<Room>

    @PUT("/api/rooms/{id}")
    fun run(@Path("id") id: Long, @Field("name") name: String)
}
