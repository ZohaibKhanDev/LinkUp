package com.example.linkup.signup

import kotlinx.coroutines.flow.Flow
interface AuthRepository {
    fun createUser(authUser: AuthUser): Flow<ResultState<String>>

    fun loginUser(authUser: AuthUser): Flow<ResultState<String>>

    fun logoutUser(): Flow<ResultState<String>>
}
