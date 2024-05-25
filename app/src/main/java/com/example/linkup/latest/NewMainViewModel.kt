package com.example.linkup.latest

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.linkup.signup.ResultState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class NewMainViewModel(private val repository: Repository):ViewModel() {
    private val _createNewUser = MutableStateFlow<ResultState<String>>(ResultState.Loading)
    val createNewuser = _createNewUser.asStateFlow()

    fun createNewuser(email: String, password: String){
        viewModelScope.launch {
            _createNewUser.value = ResultState.Loading
            try {
                val response = repository.createNewUser(email, password)
                _createNewUser.value = ResultState.Success(response)
            }catch (e: Exception){
                _createNewUser.value = ResultState.Error(e)
            }
        }
    }
}