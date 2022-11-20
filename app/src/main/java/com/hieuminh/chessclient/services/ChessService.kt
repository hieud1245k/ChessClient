package com.hieuminh.chessclient.services

import com.hieuminh.chessclient.models.Room
import com.hieuminh.chessclient.models.response.BaseResponse
import kotlinx.coroutines.Deferred
import org.json.JSONObject
import retrofit2.http.*

interface ChessService {
    @POST("api/players/name")
    fun saveNameAsync(@Query("name") name: String): Deferred<BaseResponse>

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

    @PUT("/api/rooms/leave")
    fun leaveRoomAsync(@Body room: Room): Deferred<Room>

    @POST("/api/rooms/start-offline-game")
    @FormUrlEncoded
    fun startOfflineGameAsync(@Field("name") name: String): Deferred<Room>

    @PUT("/api/rooms/play-now")
    @FormUrlEncoded
    fun playNowAsync(@Field("name") name: String): Deferred<Room>
}
