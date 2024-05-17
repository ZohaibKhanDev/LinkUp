package com.example.linkup.signup

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.module.dsl.withOptions
import org.koin.core.qualifier.qualifier
import org.koin.dsl.module

val appModule= module {
    single {
        FirebaseAuth.getInstance()
    }

    single<AuthRepository> {
        AuthRepositoryImpl(get())
    }


    viewModelOf(::AuthViewModel)

    single<DatabaseReference> {
        FirebaseDatabase.getInstance().reference
    } withOptions {
        qualifier("FirebaseDb")
    }


}