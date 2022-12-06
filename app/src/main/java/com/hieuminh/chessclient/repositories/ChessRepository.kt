package com.hieuminh.chessclient.repositories

import com.hieuminh.chessclient.models.Room

interface ChessRepository {
    suspend fun getRoomList(): Result<List<Room>>

    suspend fun createNewRoom(playerId: Long): Result<Room>

    suspend fun joinRoom(id: Long, playerId: Long): Result<Room>

    suspend fun leaveRoom(room: Room): Result<Room>

    suspend fun startOfflineGame(playerId: Long): Result<Room>

    suspend fun playNow(playerId: Long): Result<Room>
}
