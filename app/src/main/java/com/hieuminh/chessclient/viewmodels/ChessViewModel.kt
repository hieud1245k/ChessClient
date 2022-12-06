package com.hieuminh.chessclient.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hieuminh.chessclient.models.Room
import com.hieuminh.chessclient.repositories.ChessRepository
import com.hieuminh.chessclient.repositories.impl.ChessRepositoryImpl
import kotlinx.coroutines.launch

class ChessViewModel(private val repository: ChessRepository = ChessRepositoryImpl()) : ViewModel() {
    fun fetchRoomList(success: (List<Room>) -> Unit) {
        viewModelScope.launch {
            val result = repository.getRoomList()

            when {
                result.isSuccess -> {
                    val roomList = result.getOrNull() ?: return@launch
                    success.invoke(roomList)
                }
                else -> {
                    Log.d("ERROR", result.exceptionOrNull()?.message ?: "")
                }
            }
        }
    }

    fun createNewRoom(playerId: Long, success: (Room) -> Unit) {
        viewModelScope.launch {
            val result = repository.createNewRoom(playerId)

            when {
                result.isSuccess -> {
                    val room = result.getOrNull() ?: return@launch
                    success(room)
                }
                else -> {
                    Log.d("ERROR", result.exceptionOrNull()?.message ?: "")
                }
            }
        }
    }

    fun joinRoom(id: Long, playerId: Long, success: (Room) -> Unit, error: (() -> Unit)? = null) {
        viewModelScope.launch {
            val result = repository.joinRoom(id, playerId)

            when {
                result.isSuccess -> {
                    val room = result.getOrNull() ?: return@launch
                    success(room)
                }
                else -> {
                    Log.d("ERROR", result.exceptionOrNull()?.message ?: "")
                    error?.invoke()
                }
            }
        }
    }

    fun leaveRoom(room: Room, success: (Room) -> Unit, error: (() -> Unit)? = null) {
        viewModelScope.launch {
            val result = repository.leaveRoom(room)

            when {
                result.isSuccess -> {
                    val baseResponse = result.getOrNull() ?: return@launch
                    success(baseResponse)
                }
                else -> {
                    error?.invoke()
                }
            }
        }
    }

    fun startOfflineGame(playerId: Long, success: (Room) -> Unit) {
        viewModelScope.launch {
            val result = repository.startOfflineGame(playerId)

            when {
                result.isSuccess -> {
                    val room = result.getOrNull() ?: return@launch
                    success(room)
                }
                else -> {
                    Log.d("ERROR", result.exceptionOrNull()?.message ?: "")
                }
            }
        }
    }

    fun playNow(playerId: Long, success: (Room) -> Unit, error: (() -> Unit)? = null) {
        viewModelScope.launch {
            val result = repository.playNow(playerId)

            when {
                result.isSuccess -> {
                    val room = result.getOrNull() ?: return@launch
                    success(room)
                }
                else -> {
                    Log.d("ERROR", result.exceptionOrNull()?.message ?: "")
                    error?.invoke()
                }
            }
        }
    }
}
