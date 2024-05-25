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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.linkup.R
import com.example.linkup.data.remote.MainViewModel
import com.example.linkup.data.remote.Repository1
import com.example.linkup.navigation.Screen
import com.example.linkup.ui.theme.LinkUpTheme
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class ChatDetail : ComponentActivity() {
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    @RequiresApi(Build.VERSION_CODES.O)
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LinkUpTheme {
                val currentDateTime: String = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
                val userId = Firebase.auth.currentUser?.uid
                val receiverId = intent.getStringExtra("USER_ID")
                val username = intent.getStringExtra("USERNAME")

                val viewModel: MainViewModel = viewModel(
                    factory = MainViewModelFactory(Repository1(Firebase.database.getReference("Message").child("chat")))
                )

                val navController = rememberNavController()
                var message by remember { mutableStateOf("") }

                Scaffold(
                    topBar = { TopBar(username, navController) },
                    bottomBar = {
                        BottomBar(
                            message = message,
                            onMessageChange = { message = it },
                            onSendMessage = {
                                val newMessage = Message(
                                    message = it,
                                    currentTimeOrDate = currentDateTime,
                                    receiverId = receiverId.toString(),
                                    senderId = userId
                                )
                                viewModel.addMessage(newMessage)
                                message = ""
                            }
                        )
                    }
                ) {
                    val messages by viewModel.messages.collectAsState()
                    ChatScreen(messages = messages, userId = userId?:"", receiverId = receiverId?:"")
                }
            }
        }
    }
}



@Composable
fun ChatScreen(messages: List<Message>, userId: String, receiverId: String) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 60.dp),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.chatheme),
            contentDescription = "Chat background",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        val filteredMessages = messages.filter {
            (it.receiverId == receiverId && it.senderId == userId) ||
                    (it.receiverId == userId && it.senderId == receiverId)
        }

        ChatContent(messages = filteredMessages, currentUserId = userId)
    }
}

@Composable
fun ChatContent(messages: List<Message>, currentUserId: String) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(5.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        items(messages) { message ->
            if (message.senderId == currentUserId) {
                SenderMessage(message = message)
            } else {
                ReceiverMessage(message = message)
            }
        }
    }
}

@Composable
fun SenderMessage(message: Message) {
    Box(
        modifier = Modifier.fillMaxWidth(),
        contentAlignment = Alignment.CenterEnd
    ) {
        Card(
            modifier = Modifier
                .widthIn(max = 250.dp)
                .wrapContentHeight(),
            shape = RoundedCornerShape(bottomEnd = 15.dp, topStart = 15.dp, topEnd = 15.dp),
            colors = CardDefaults.cardColors(Color(0XFF04ba62))
        ) {
            Column(modifier = Modifier.padding(10.dp)) {
                Text(text = message.message, color = Color.White)
                Spacer(modifier = Modifier.height(5.dp))
                Text(text = message.currentTimeOrDate, color = Color.White, fontSize = 10.sp)
            }
        }
    }
}

@Composable
fun ReceiverMessage(message: Message) {
    Box(
        modifier = Modifier.fillMaxWidth(),
        contentAlignment = Alignment.CenterStart
    ) {
        Card(
            modifier = Modifier
                .widthIn(max = 250.dp)
                .wrapContentHeight(),
            shape = RoundedCornerShape(bottomEnd = 15.dp, bottomStart = 15.dp, topEnd = 15.dp),
            colors = CardDefaults.cardColors(Color(0XFF00aeff).copy(alpha = 0.4f))
        ) {
            Column(modifier = Modifier.padding(10.dp)) {
                Text(text = message.message, color = Color.Black)
                Spacer(modifier = Modifier.height(5.dp))
                Text(text = message.currentTimeOrDate, color = Color.Black, fontSize = 10.sp)
            }
        }
    }
}

@Composable
fun BottomBar(
    message: String,
    onMessageChange: (String) -> Unit,
    onSendMessage: (String) -> Unit
) {
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
            onValueChange = onMessageChange,
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
                    .clickable { onSendMessage(message) }
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(username: String?, navController: NavHostController, ) {

    val navController = rememberNavController()

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
}


data class Message(
    val message: String = "",
    val currentTimeOrDate: String = "",
    val receiverId: String = "",
    val senderId: String? = "",
)