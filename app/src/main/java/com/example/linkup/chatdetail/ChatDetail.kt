package com.example.linkup.chatdetail

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.AbsoluteAlignment
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Green
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.rememberNavController
import com.example.linkup.R
import com.example.linkup.data.remote.MainViewModel
import com.example.linkup.data.remote.Repository
import com.example.linkup.navigation.Screen
import com.example.linkup.ui.theme.LinkUpTheme
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class ChatDetail : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LinkUpTheme {
                val currentDateTime: String =
                    LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
                val userId = Firebase.auth.currentUser?.uid
                val messRef = Firebase.database.getReference("Message").child("chat")
                val repository = remember { Repository(messRef) }
                val viewModel = remember { MainViewModel(repository) }
                var message by remember { mutableStateOf("") }
                var messages by remember { mutableStateOf(listOf<Message>()) }

                val navController = rememberNavController()
                val username = intent.getStringExtra("USERNAME")
                val receiverId = intent.getStringExtra("USER_ID")

                LaunchedEffect(Unit) {
                    val userListener = object : ValueEventListener {
                        override fun onDataChange(snapshot: DataSnapshot) {
                            val chatList = mutableListOf<Message>()
                            for (userSnapshot in snapshot.children) {
                                val message = userSnapshot.getValue(Message::class.java)
                                if (message != null) {
                                    chatList.add(message)
                                }
                            }
                            messages = chatList
                        }

                        override fun onCancelled(error: DatabaseError) {

                        }
                    }
                    messRef.addValueEventListener(userListener)
                }

                Scaffold(
                    topBar = {
                        TopAppBar(
                            title = {
                                Column(horizontalAlignment = Alignment.Start) {
                                    Text(text = "$username", fontSize = 20.sp)
                                    Text(
                                        text = "°Online",
                                        color = Color.Green,
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.Medium
                                    )
                                }
                            },
                            navigationIcon = {
                                Row(
                                    modifier = Modifier
                                        .wrapContentWidth()
                                        .clickable { navController.navigate(Screen.Chat.route) },
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
                            },
                            actions = {
                                Box(
                                    modifier = Modifier
                                        .clip(CircleShape)
                                        .size(35.dp)
                                        .background(Color.LightGray.copy(alpha = 0.50f)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Call,
                                        contentDescription = ""
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
                                        contentDescription = ""
                                    )
                                }
                            }
                        )
                    },
                    bottomBar = {
                        BottomAppBar {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceAround,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    modifier = Modifier
                                        .clip(CircleShape)
                                        .size(40.dp)
                                        .background(Color.LightGray.copy(alpha = 0.50f)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Image(
                                        painter = painterResource(id = R.drawable.emojii),
                                        contentDescription = "",
                                        contentScale = ContentScale.Crop,
                                        modifier = Modifier.size(25.dp)
                                    )
                                }

                                TextField(
                                    value = message,
                                    onValueChange = {
                                        message = it
                                    },
                                    placeholder = {
                                        Text(text = "Type your message...")
                                    },
                                    shape = RoundedCornerShape(10.dp),
                                    colors = TextFieldDefaults.colors(
                                        focusedIndicatorColor = Color.White,
                                        unfocusedIndicatorColor = Color.White
                                    )
                                )
                                if (message.isBlank()) {
                                    Box(
                                        modifier = Modifier
                                            .clip(CircleShape)
                                            .size(40.dp)
                                            .background(Color.LightGray.copy(alpha = 0.50f)),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.KeyboardVoice,
                                            contentDescription = "",
                                            modifier = Modifier.size(25.dp)
                                        )
                                    }
                                } else {
                                    Box(
                                        modifier = Modifier
                                            .clickable {
                                                val newMessage = Message(
                                                    message,
                                                    currentDateTime,
                                                    receiverId.toString(),
                                                    userId.toString(),

                                                    )
                                                messRef
                                                    .push()
                                                    .setValue(newMessage)
                                                message = ""
                                            }
                                            .clip(CircleShape)
                                            .size(40.dp)
                                            .background(Color.LightGray.copy(alpha = 0.50f)),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Send,
                                            contentDescription = "",
                                            modifier = Modifier.size(25.dp),
                                            tint = Color(0XFF00aeff)
                                        )
                                    }
                                }
                            }
                        }
                    }
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(top = it.calculateTopPadding()),
                        contentAlignment = Alignment.Center
                    ) {

                        Image(
                            painter = painterResource(id = R.drawable.chatheme),
                            contentDescription = "",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )
                        val fitlerData = messages.filter {
                            it.receiverId == receiverId.toString() && it.senderId == userId.toString()
                        }
                        ChatContent(messages = fitlerData)
                    }
                }
            }
        }
    }
}

@Composable
fun ChatContent(messages: List<Message>) {
    val navController= rememberNavController()
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(5.dp), verticalArrangement = Arrangement.spacedBy(5.dp)
    ) {
        items(messages) { message ->
            if (message.senderId == message.senderId) {
                Column(
                    modifier = Modifier.padding(top = 50.dp),
                    horizontalAlignment = AbsoluteAlignment.Left
                ) {
                    Card(
                        modifier = Modifier
                            .width(100.dp)
                            .wrapContentHeight(),
                        shape = RoundedCornerShape(
                            bottomStart = 10.dp,
                            topStart = 10.dp,
                            topEnd = 10.dp
                        ), colors = CardDefaults.cardColors(Green)
                    ) {
                        Text(
                            text = "${message.message}",
                            modifier = Modifier
                                .padding(10.dp)

                        )
                    }
                }
            } else {
                Column(
                    modifier = Modifier.padding(top = 50.dp),
                    horizontalAlignment = AbsoluteAlignment.Right
                ) {
                    Card(
                        modifier = Modifier
                            .width(100.dp)
                            .wrapContentHeight(),
                        shape = RoundedCornerShape(
                            bottomStart = 10.dp,
                            topStart = 10.dp,
                            topEnd = 10.dp
                        )
                    ) {
                        Text(
                            text = "${message.message}",
                            modifier = Modifier
                                .padding(10.dp)
                        )
                    }
                }
            }


        }
    }
}

data class Message(
    val message: String = "",
    val currentTimeOrDate: String = "",
    val receiverId: String = "",
    val senderId: String? = "",
)