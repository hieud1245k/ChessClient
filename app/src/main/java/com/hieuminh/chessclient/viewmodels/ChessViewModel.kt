package com.hieuminh.chessclient.viewmodels

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hieuminh.chessclient.models.Player
import com.hieuminh.chessclient.models.Room
import com.hieuminh.chessclient.models.response.BaseResponse
import com.hieuminh.chessclient.repositories.ChessRepository
import com.hieuminh.chessclient.repositories.impl.ChessRepositoryImpl
import kotlinx.coroutines.launch

class ChessViewModel(private val repository: ChessRepository = ChessRepositoryImpl()) : ViewModel() {
    val roomListLiveData = MutableLiveData<List<Room>>()

    fun fetchRoomList() {
        viewModelScope.launch {
            val result = repository.getRoomList()

            when {
                result.isSuccess -> {
                    val roomList = result.getOrNull() ?: return@launch
                    roomListLiveData.postValue(roomList)
                }
                else -> {
                    Log.d("ERROR", result.exceptionOrNull()?.message ?: "")
                }
            }
        }
    }

    fun createNewRoom(name: String, success: (Room) -> Unit) {
        viewModelScope.launch {
            val result = repository.createNewRoom(name)

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

    fun joinRoom(id: Long, name: String, success: (Room) -> Unit, error: (() -> Unit)? = null) {
        viewModelScope.launch {
            val result = repository.joinRoom(id, name)

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

    fun saveName(name: String, success: (BaseResponse) -> Unit, error: (() -> Unit)? = null) {
        viewModelScope.launch {
            val result = repository.saveName(name)

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

    fun startOfflineGame(name: String, level: Int, success: (Room) -> Unit) {
        viewModelScope.launch {
            val player = Player().apply {
                this.name = name
                this.level = level
            }
            val result = repository.startOfflineGame(player)

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

    fun playNow(name: String, success: (Room) -> Unit, error: (() -> Unit)? = null) {
        viewModelScope.launch {
            val result = repository.playNow(name)

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

    fun kickTheOpponent(room: Room, rivalName: String, success: (Room) -> Unit) {
        viewModelScope.launch {
            val result = repository.kickTheOpponent(room, rivalName)
            when {
                result.isSuccess -> {
                    val roomResponse = result.getOrNull() ?: return@launch
                    success(roomResponse)
                }
                else -> {
                    Log.d("ERROR", result.exceptionOrNull()?.message ?: "")
                }
            }
        }
    }
}
