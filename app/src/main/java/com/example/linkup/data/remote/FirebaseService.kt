package com.example.linkup.data.remote

import com.example.linkup.User
import com.example.linkup.chatdetail.Message

interface FirebaseService {
    suspend fun writeUserData(user: User,userId: String): String
    suspend fun getUserData(userId: String): User?

    suspend fun writeUserMessage(message: Message,userId: String):String

}

