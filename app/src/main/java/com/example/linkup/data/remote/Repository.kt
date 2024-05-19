package com.example.linkup.data.remote

import androidx.compose.runtime.mutableStateOf
import com.example.linkup.User
import com.example.linkup.chatdetail.Message
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.getValue

class Repository(private val dbRef: DatabaseReference) : FirebaseService {
    override suspend fun writeUserData(user: User, userId: String): String {
        val isSuccessful = mutableStateOf("")
        val myRef = dbRef.database.getReference("Users")
        myRef.child(userId).setValue(user).addOnSuccessListener {
            isSuccessful.value = "User Data Stored Successfully"
        }.addOnFailureListener {
            isSuccessful.value = "Error while storing Data"
        }
        return isSuccessful.value
    }

    override suspend fun getUserData(userId: String): User? {
        val isSuccessful = mutableStateOf<User?>(null)

        val myRef = dbRef.database.getReference("Users")
        myRef.child(userId).get().addOnSuccessListener {
            val user = it.getValue<User>()
            isSuccessful.value = user
        }
        return isSuccessful.value
    }

    override suspend fun writeUserMessage(message: Message, userId: String): String {
        val isSuccessful = mutableStateOf("")
        val messRef = dbRef.database.getReference("Message")
        messRef.child("chat$userId").child(userId).setValue(message).addOnSuccessListener {
            isSuccessful.value = "User Data Stored Successfully"
        }.addOnFailureListener {
            isSuccessful.value = "${it.message}"
        }
        return isSuccessful.value
    }
}

