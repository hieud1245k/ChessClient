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

    override suspend fun createNewRoom(playerId: Long): Result<Room> {
        return try {
            Result.success(service.createNewRoomAsync(playerId).await())
        } catch (throwable: Throwable) {
            Result.failure(throwable)
        }
    }

    override suspend fun joinRoom(roomId: Long, playerId: Long): Result<Room> {
        return try {
            Result.success(service.joinRoomAsync(roomId, playerId).await())
        } catch (throwable: Throwable) {
            Result.failure(throwable)
        }
    }

    override suspend fun leaveRoom(room: Room): Result<Room> {
        return try {
            Result.success(service.leaveRoomAsync(room).await())
        } catch (throwable: Throwable) {
            Result.failure(throwable)
        }
    }

    override suspend fun startOfflineGame(playerId: Long): Result<Room> {
        return try {
            Result.success(service.startOfflineGameAsync(playerId).await())
        } catch (throwable: Throwable) {
            Result.failure(throwable)
        }
    }

    override suspend fun playNow(playerId: Long): Result<Room> {
        return try {
            Result.success(service.playNowAsync(playerId).await())
        } catch (throwable: Throwable) {
            Result.failure(throwable)
        }
    }
}
