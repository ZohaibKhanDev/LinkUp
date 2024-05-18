package com.example.linkup.signup

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIos
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.linkup.data.remote.MainViewModel
import com.example.linkup.data.remote.Repository
import com.example.linkup.navigation.Screen
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.database.database
import kotlinx.coroutines.launch
import org.koin.compose.koinInject

@Composable
fun SignUpScreen(navController: NavController) {
    val viewModel1: AuthViewModel = koinInject()
    val dbRef = Firebase.database.getReference("Users")
    val repository = remember {
        Repository(dbRef)
    }
    val viewModel = remember {
        MainViewModel(repository)
    }
    val userId = Firebase.auth.currentUser?.uid ?: ""


    var name by remember {
        mutableStateOf("")
    }

    var email by remember {
        mutableStateOf("")
    }

    var password by remember {
        mutableStateOf("")
    }

    val scope = rememberCoroutineScope()


    val context = LocalContext.current

    var isDialog by remember {
        mutableStateOf(false)
    }

    var visibility by remember {
        mutableStateOf(false)
    }


    if (isDialog) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
    }

    Box(
        modifier = Modifier
            .clip(
                CircleShape
            )
            .padding(14.dp)
            .size(35.dp)
            .background(Color(0XFFE8ECF4).copy(alpha = 0.60f)), contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = Icons.Default.ArrowBackIos,
            contentDescription = "",
        )
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 120.dp, start = 20.dp, end = 20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = "Sign UP",
            fontSize = 25.sp,
            fontWeight = FontWeight.Medium,
            color = Color(0XFF1F41BB)
        )
        Spacer(modifier = Modifier.height(20.dp))
        TextField(
            value = name,
            onValueChange = {
                name = it
            },
            placeholder = {
                Text(text = "Enter Your Name")
            },
            colors = TextFieldDefaults.colors(
                focusedIndicatorColor = Color(0XFFE8ECF4),
                unfocusedIndicatorColor = Color(0XFFE8ECF4),
                focusedContainerColor = Color(0XFFE8ECF4),
                unfocusedContainerColor = Color(0XFFE8ECF4),
            ), modifier = Modifier
                .width(300.dp)
                .height(50.dp), shape = RoundedCornerShape(9.dp)
        )


        Spacer(modifier = Modifier.height(20.dp))

        TextField(value = email, onValueChange = {
            email = it
        }, placeholder = {
            Text(text = "Enter Email")
        }, colors = TextFieldDefaults.colors(
            focusedIndicatorColor = Color(0XFFE8ECF4),
            unfocusedIndicatorColor = Color(0XFFE8ECF4),
            focusedContainerColor = Color(0XFFE8ECF4),
            unfocusedContainerColor = Color(0XFFE8ECF4),
        ), modifier = Modifier
            .width(300.dp)
            .height(50.dp), shape = RoundedCornerShape(9.dp)
        )


        Spacer(modifier = Modifier.height(20.dp))

        TextField(
            value = password,
            onValueChange = {
                password = it
            },
            placeholder = {
                Text(text = "Enter Password")
            },
            colors = TextFieldDefaults.colors(
                focusedIndicatorColor = Color(0XFFE8ECF4),
                unfocusedIndicatorColor = Color(0XFFE8ECF4),
                focusedContainerColor = Color(0XFFE8ECF4),
                unfocusedContainerColor = Color(0XFFE8ECF4),
            ),
            modifier = Modifier
                .width(300.dp)
                .height(50.dp),
            shape = RoundedCornerShape(9.dp),
            trailingIcon = {
                if (password >= 1.toString()) {

                    Icon(
                        imageVector = if (visibility) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                        contentDescription = "",
                        modifier = Modifier.clickable { visibility = !visibility })
                }


            },
            visualTransformation = if (visibility) VisualTransformation.None else PasswordVisualTransformation()


        )


        Spacer(modifier = Modifier.height(25.dp))

        Button(
            onClick = {

                navController.navigate(Screen.Home.route)
                val user = com.example.linkup.User(null, name, email, password, "male", "pk")
                viewModel.storeData(user, userId)
                val sharedPreferences = context.getSharedPreferences("Linkup",Context.MODE_PRIVATE)
                sharedPreferences.edit().putString("userId",userId).apply()
                println("SHARED ${Firebase.auth.currentUser}")
                scope.launch {
                    viewModel1.createUser(
                        AuthUser(
                            email, password
                        )
                    ).collect {
                        when (it) {
                            is ResultState.Error -> {
                                Toast.makeText(context, "${it.error}", Toast.LENGTH_SHORT).show()
                                isDialog = false
                            }

                            ResultState.Loading -> {
                                isDialog = true
                            }

                            is ResultState.Success -> {
                                Toast.makeText(context, "${it.response}", Toast.LENGTH_SHORT).show()
                                isDialog = false

                            }
                        }
                    }
                }
            }, modifier = Modifier
                .width(273.dp)
                .height(50.dp), shape = RoundedCornerShape(5.dp)
        ) {
            Text(text = "Sign Up")
        }
    }


}