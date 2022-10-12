package com.hieuminh.chessclient.repositories

import com.hieuminh.chessclient.models.Room

interface ChessRepository {
    suspend fun getRoomList(): Result<List<Room>>

    suspend fun createNewRoom(name: String): Result<Room>

    suspend fun joinRoom(id: Long, name: String): Result<Room>
}
