package com.example.linkup.data.remote

import com.example.linkup.chatdetail.Message
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.getValue
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow


import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class MainViewModel(private val repository: Repository1) : ViewModel() {
    private val _messages = MutableStateFlow<List<Message>>(emptyList())
    val messages: StateFlow<List<Message>> get() = _messages

    init {
        viewModelScope.launch {
            repository.getMessages().collect {
                Log.d("MainViewModel", "Messages retrieved: $it")
                _messages.value = it
            }
        }
    }

    fun addMessage(message: Message) {
        viewModelScope.launch {
            repository.addMessage(message)
        }
    }
}


class Repository1(private val databaseReference: DatabaseReference) {

    fun getMessages(): Flow<List<Message>> = callbackFlow {
        val listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val messages = snapshot.children.mapNotNull { it.getValue<Message>() }
                trySend(messages)
            }

            override fun onCancelled(error: DatabaseError) {
                close(error.toException())
            }
        }
        databaseReference.addValueEventListener(listener)
        awaitClose { databaseReference.removeEventListener(listener) }
    }

    suspend fun addMessage(message: Message) {
        databaseReference.push().setValue(message)
    }
}
