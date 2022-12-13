package com.hieuminh.chessclient.repositories.impl

import com.hieuminh.chessclient.models.Player
import com.hieuminh.chessclient.models.Room
import com.hieuminh.chessclient.models.response.BaseResponse
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

    override suspend fun saveName(name: String): Result<BaseResponse> {
        return try {
            Result.success(service.saveNameAsync(name).await())
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

    override suspend fun startOfflineGame(player: Player): Result<Room> {
        return try {
            Result.success(service.startOfflineGameAsync(player).await())
        } catch (throwable: Throwable) {
            Result.failure(throwable)
        }
    }

    override suspend fun playNow(name: String): Result<Room> {
        return try {
            Result.success(service.playNowAsync(name).await())
        } catch (throwable: Throwable) {
            Result.failure(throwable)
        }
    }

    override suspend fun kickTheOpponent(room: Room, rivalName: String): Result<Room> {
        return try {
            Result.success(service.kickTheOpponentAsync(room, rivalName).await())
        } catch (throwable: Throwable) {
            Result.failure(throwable)
        }
    }
}
