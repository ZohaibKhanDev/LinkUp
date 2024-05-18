package com.example.linkup

import NavEntry
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.linkup.navigation.Navigation
import com.example.linkup.signup.appModule
import com.example.linkup.ui.theme.LinkUpTheme
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        startKoin {
            androidContext(this@MainActivity)
            androidLogger()
            modules(appModule)
        }
        setContent {

            LinkUpTheme {
             NavEntry()


            }
        }
    }

}


data class User(
    val id: String? = "",
    val usernames: String? = "",
    val email: String? = "",
    val password: String? = "",
    val gender: String? = "",
    val country: String = ""
)
