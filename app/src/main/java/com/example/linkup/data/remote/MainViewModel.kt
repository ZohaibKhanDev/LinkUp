package com.example.linkup.data.remote

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.linkup.User
import com.example.linkup.chatdetail.Message
import com.example.linkup.signup.ResultState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MainViewModel(private val repository: Repository) : ViewModel() {
    val _writeUser = MutableStateFlow<ResultState<String>>(ResultState.Loading)

    val writeUser: StateFlow<ResultState<String>> = _writeUser.asStateFlow()

    val _getData=MutableStateFlow<ResultState<User?>>(ResultState.Loading)
    val getData:StateFlow<ResultState<User?>> = _getData.asStateFlow()

    val _writeMessage=MutableStateFlow<ResultState<String>>(ResultState.Loading)

    val writeMessage:StateFlow<ResultState<String>> = _writeMessage.asStateFlow()

    fun storedMessage(message: Message,userId: String){
        _writeMessage.value=ResultState.Loading
        viewModelScope.launch {
            try {
                val response=repository.writeUserMessage(message,userId)
                _writeMessage.value=ResultState.Success(response)
            }catch (e:Exception){
                _writeMessage.value=ResultState.Error(e)
            }
        }
    }

    fun getData(user: String){
        viewModelScope.launch {
            _getData.value=ResultState.Loading
            try {
                val response=repository.getUserData(user)
                _getData.value= ResultState.Success(response)
            }catch (e:Exception){
                _getData.value=ResultState.Error(e)
            }
        }
    }

    fun storeData(user: User, userId: String) {
        _writeUser.value=ResultState.Loading
        viewModelScope.launch {
            try {
                val response = repository.writeUserData(user,userId)
                _writeUser.value = ResultState.Success(response)
            } catch (e: Exception) {
                _writeUser.value = ResultState.Error(e)
            }
        }
    }
}