package com.hieuminh.chessclient.repositories.impl

import com.hieuminh.chessclient.models.Room
import com.hieuminh.chessclient.providers.ApiClientProvider
import com.hieuminh.chessclient.repositories.ChessRepository
import com.hieuminh.chessclient.services.ChessService

class ChessRepositoryImpl(private val service: ChessService = ApiClientProvider.getService()) : ChessRepository {
    override suspend fun getRoomList(): Result<List<Room>> {
        return try {
            Result.success(service.getRoomList().await())
        } catch (throwable: Throwable) {
            Result.failure(throwable)
        }
    }
}
