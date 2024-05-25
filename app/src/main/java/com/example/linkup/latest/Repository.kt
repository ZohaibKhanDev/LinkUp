package com.example.linkup.latest

import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.ktx.Firebase

class Repository {
    suspend fun createNewUser(email: String, password: String):String{
        var message = ""
        val firebaseAuth = FirebaseAuth.getInstance()
        firebaseAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener {
            if (it.isSuccessful) {
                message = "Success"
            }else{
                message = it.exception?.message.toString()
            }
        }
        return message
    }
}