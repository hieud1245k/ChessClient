package com.hieuminh.chessclient.viewmodels

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hieuminh.chessclient.models.Room
import com.hieuminh.chessclient.models.response.BaseResponse
import com.hieuminh.chessclient.repositories.ChessRepository
import com.hieuminh.chessclient.repositories.impl.ChessRepositoryImpl
import kotlinx.coroutines.launch

class ChessViewModel(private val repository: ChessRepository = ChessRepositoryImpl()) : ViewModel() {
    val roomListLiveData = MutableLiveData<List<Room>>()
    val newRoomLiveData = MutableLiveData<Room>()
    val joinRoomLiveData = MutableLiveData<Room>()

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

    fun createNewRoom(name: String) {
        viewModelScope.launch {
            val result = repository.createNewRoom(name)

            when {
                result.isSuccess -> {
                    val room = result.getOrNull() ?: return@launch
                    newRoomLiveData.postValue(room)
                }
                else -> {
                    Log.d("ERROR", result.exceptionOrNull()?.message ?: "")
                }
            }
        }
    }

    fun joinRoom(id: Long, name: String) {
        viewModelScope.launch {
            val result = repository.joinRoom(id, name)

            when {
                result.isSuccess -> {
                    val room = result.getOrNull() ?: return@launch
                    joinRoomLiveData.postValue(room)
                }
                else -> {
                    Log.d("ERROR", result.exceptionOrNull()?.message ?: "")
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
}
