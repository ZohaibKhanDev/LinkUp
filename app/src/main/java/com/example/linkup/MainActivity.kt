package com.example.linkup

import NavEntry
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.linkup.data.remote.MainViewModel
import com.example.linkup.latest.NewMainViewModel
import com.example.linkup.latest.Repository
import com.example.linkup.signup.ResultState
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
               /* var email by remember {
                    mutableStateOf("")
                }
                var pass by remember {
                    mutableStateOf("")
                }
                var dataByServer by remember {
                    mutableStateOf("")
                }
                var isLoading by remember {
                    mutableStateOf(false)
                }
                val viewModel = remember {
                    NewMainViewModel(com.example.linkup.data.remote.Repository())
                }
                val state by viewModel.createNewuser.collectAsState()
                when (state) {
                    is ResultState.Error -> {
                        val error = (state as ResultState.Error).error
                        dataByServer = error.toString()
                    }

                    ResultState.Loading -> {
                        if (isLoading) {
                            CircularProgressIndicator()
                        }
                    }

                    is ResultState.Success -> {
                        val response = (state as ResultState.Success).response
                        dataByServer = response
                    }
                }
                //NavEntry()

                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    TextField(
                        value = email,
                        onValueChange = { email = it },
                        placeholder = { Text(text = "Email") })
                    TextField(
                        value = pass,
                        onValueChange = { pass = it },
                        placeholder = { Text(text = "Password") })
                    Button(onClick = {
                        viewModel.createNewuser(email, pass)
                    }) {
                        Text(text = "Create Account")
                    }
                }*/


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
