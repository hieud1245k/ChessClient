package com.hieuminh.chessclient.repositories

import com.hieuminh.chessclient.models.Player
import com.hieuminh.chessclient.models.Room
import com.hieuminh.chessclient.models.response.BaseResponse

interface ChessRepository {
    suspend fun getRoomList(): Result<List<Room>>

    suspend fun createNewRoom(name: String): Result<Room>

    suspend fun joinRoom(id: Long, name: String): Result<Room>

    suspend fun saveName(name: String): Result<BaseResponse>

    suspend fun leaveRoom(room: Room): Result<Room>

    suspend fun startOfflineGame(player: Player): Result<Room>

    suspend fun playNow(name: String): Result<Room>

    suspend fun kickTheOpponent(room: Room, rivalName: String): Result<Room>
}
