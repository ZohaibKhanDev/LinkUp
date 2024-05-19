package com.example.linkup.chatdetail

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Videocam
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.rememberNavController
import com.example.linkup.navigation.Screen
import com.example.linkup.ui.theme.LinkUpTheme

class ChatDetail : ComponentActivity() {
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LinkUpTheme {
                val navController= rememberNavController()
                val userId = intent.getStringExtra("USER_ID")
                val username = intent.getStringExtra("USERNAME")
                Scaffold(topBar = {
                    TopAppBar(title = {
                        Column(
                            horizontalAlignment = Alignment.Start
                        ) {
                            Text(text = "$username", fontSize = 20.sp)
                            Text(
                                text = "°Online",
                                color = Color.Green,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }, navigationIcon = {
                        Row(
                            modifier = Modifier
                                .wrapContentWidth()
                                .clickable { navController.navigate(Screen.Chat.route)},
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.ArrowBackIosNew,
                                contentDescription = ""
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Box(
                                modifier = Modifier
                                    .clip(CircleShape)
                                    .size(50.dp)
                                    .background(Color.LightGray),
                                contentAlignment = Alignment.Center
                            ) {
                                Image(
                                    imageVector = Icons.Default.Person,
                                    contentDescription = "",
                                    contentScale = ContentScale.Crop
                                )
                            }
                        }
                    }, actions = {
                        Box(
                            modifier = Modifier
                                .clip(CircleShape)
                                .size(35.dp)
                                .background(Color.LightGray.copy(alpha = 0.50f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Call,
                                contentDescription = "",

                            )
                        }

                        Spacer(modifier = Modifier.width(10.dp))

                        Box(
                            modifier = Modifier
                                .clip(CircleShape)
                                .size(35.dp)
                                .background(Color.LightGray.copy(alpha = 0.50f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Videocam,
                                contentDescription = "",

                            )
                        }
                    })
                }) {

                }
            }
        }
    }
}
