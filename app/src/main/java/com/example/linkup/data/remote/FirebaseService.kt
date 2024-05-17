package com.example.linkup.data.remote

import com.example.linkup.User
import io.getstream.chat.android.models.UserId

interface FirebaseService {
    suspend fun writeUserData(user: User,userId: String): String
    suspend fun getUserData(userId: String): User?
}

