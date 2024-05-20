package com.example.linkup.signup

import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class AuthRepositoryImpl(private val authdb: FirebaseAuth) : AuthRepository {
    override fun createUser(authUser: AuthUser): Flow<ResultState<String>> = callbackFlow {
        trySend(ResultState.Loading)
        authdb.createUserWithEmailAndPassword(authUser.email, authUser.password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    trySend(ResultState.Success("Create User Successfully"))
                } else {
                    trySend(ResultState.Error(task.exception ?: Exception("Unknown Error")))
                }
            }.addOnFailureListener {
                trySend(ResultState.Error(it))
            }
        awaitClose { close() }
    }

    override fun loginUser(authUser: AuthUser): Flow<ResultState<String>> = callbackFlow {
        trySend(ResultState.Loading)
        authdb.signInWithEmailAndPassword(authUser.email, authUser.password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    trySend(ResultState.Success("Login Successfully"))
                } else {
                    trySend(ResultState.Error(task.exception ?: Exception("Unknown Error")))
                }
            }.addOnFailureListener {
                trySend(ResultState.Error(it))
            }
        awaitClose { close() }
    }
}
