package com.example.linkup.signup

import androidx.lifecycle.ViewModel

class AuthViewModel(private val repo: AuthRepository) : ViewModel() {

    fun createUser(authUser: AuthUser) = repo.createUser(authUser)

    fun loginUser(authUser: AuthUser) = repo.loginUser(authUser)

    fun logoutUser() = repo.logoutUser()
}
