package com.hieuminh.chessclient.repositories.impl

import com.hieuminh.chessclient.models.Room
import com.hieuminh.chessclient.providers.ApiClientProvider
import com.hieuminh.chessclient.repositories.ChessRepository
import com.hieuminh.chessclient.services.ChessService

class ChessRepositoryImpl(private val service: ChessService = ApiClientProvider.getService()) : ChessRepository {
    override suspend fun getRoomList(): Result<List<Room>> {
        return try {
            val roomList = service.getRoomListAsync().await()
            Result.success(roomList)
        } catch (throwable: Throwable) {
            Result.failure(throwable)
        }
    }

    override suspend fun createNewRoom(name: String): Result<Room> {
        return try {
            Result.success(service.createNewRoomAsync(name).await())
        } catch (throwable: Throwable) {
            Result.failure(throwable)
        }
    }

    override suspend fun joinRoom(id: Long, name: String): Result<Room> {
        return try {
            Result.success(service.joinRoomAsync(id, name).await())
        } catch (throwable: Throwable) {
            Result.failure(throwable)
        }
    }
}
