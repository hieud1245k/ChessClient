package com.hieuminh.chessclient.services

import com.hieuminh.chessclient.models.Room
import kotlinx.coroutines.Deferred
import retrofit2.http.*

interface ChessService {
    @GET("api/rooms/")
    fun getRoomListAsync(): Deferred<List<Room>>

    @POST("api/rooms/")
    @FormUrlEncoded
    fun createNewRoomAsync(@Field("player_id") playerId: Long): Deferred<Room>

    @PUT("api/rooms/{id}")
    @FormUrlEncoded
    fun joinRoomAsync(@Path("id") roomId: Long, @Field("player_id") playerId: Long): Deferred<Room>

    @PUT("/api/rooms/{id}")
    fun run(@Path("id") roomId: Long, @Field("player_id") playerId: Long)

    @PUT("/api/rooms/leave")
    fun leaveRoomAsync(@Body room: Room): Deferred<Room>

    @POST("/api/rooms/start-offline-game")
    @FormUrlEncoded
    fun startOfflineGameAsync(@Field("player_id") playerId: Long): Deferred<Room>

    @PUT("/api/rooms/play-now")
    @FormUrlEncoded
    fun playNowAsync(@Field("player_id") playerId: Long): Deferred<Room>
}
