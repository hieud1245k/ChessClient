package com.hieuminh.chessclient.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hieuminh.chessclient.models.Room
import com.hieuminh.chessclient.repositories.ChessRepository
import com.hieuminh.chessclient.repositories.impl.ChessRepositoryImpl
import kotlinx.coroutines.launch

class ChessViewModel(private val repository: ChessRepository = ChessRepositoryImpl()) : ViewModel() {
    private val roomListLiveData = MutableLiveData<List<Room>>()
    private val newRoomLiveData = MutableLiveData<Room>()

    fun fetchRoomList() : MutableLiveData<List<Room>> {
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
        return roomListLiveData
    }

    fun createNewRoom(): MutableLiveData<Room> {
        viewModelScope.launch {
            val result = repository.createNewRoom()

            when {
                result.isSuccess -> {
                    val room = result.getOrNull() ?: return@launch
                    newRoomLiveData.postValue(room                                                                                                                                                                                                              )
                }
                else -> {
                    Log.d("ERROR", result.exceptionOrNull()?.message ?: "")
                }
            }
        }
        return newRoomLiveData
    }
}
