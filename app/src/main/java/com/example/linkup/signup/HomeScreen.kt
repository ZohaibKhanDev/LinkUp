@file:Suppress("NAME_SHADOWING")

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DoneAll
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.linkup.User
import com.example.linkup.chatdetail.ChatDetail
import com.example.linkup.navigation.Navigation
import com.example.linkup.navigation.Screen
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun HomeScreen(navController: NavController) {
    val context = LocalContext.current
    val sharedPreferences = context.getSharedPreferences("Linkup", Context.MODE_PRIVATE)
    var sharedUserName = sharedPreferences.getString("name", null)
    var sharedUserId = sharedPreferences.getString("userId", null)

    val userId = Firebase.auth.currentUser?.uid ?: ""
    var search by remember { mutableStateOf("") }
    var users by remember { mutableStateOf(listOf<User>()) }

    LaunchedEffect(Unit) {
        val dbRef = FirebaseDatabase.getInstance().getReference("Users")

        val userListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val userList = mutableListOf<User>()
                for (userSnapshot in snapshot.children) {
                    val user = userSnapshot.getValue(User::class.java)
                    if (user != null) {
                        userList.add(user)
                    }
                }
                users = userList
            }

            override fun onCancelled(error: DatabaseError) {

            }
        }
        dbRef.addValueEventListener(userListener)
    }

    val filteredUsers = users.filter {
        it.usernames?.contains(search, ignoreCase = true) == true
    }
    var drop by remember {
        mutableStateOf(false)
    }

    Scaffold(topBar = {
        LargeTopAppBar(
            title = {
                TextField(
                    value = search,
                    onValueChange = { search = it },
                    placeholder = { Text(text = "Search") },
                    trailingIcon = {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = ""
                        )
                    },
                    modifier = Modifier
                        .padding(14.dp)
                        .height(52.dp)
                        .width(350.dp),
                    singleLine = true,
                    shape = RoundedCornerShape(40.dp),
                    textStyle = TextStyle(
                        fontSize = 13.sp
                    ),
                    colors = TextFieldDefaults.textFieldColors(
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent
                    )
                )
            },
            navigationIcon = {
                Text(text = "$sharedUserName", fontSize = 22.sp, fontWeight = FontWeight.SemiBold)
            }, actions = {
                Icon(
                    imageVector = Icons.Default.MoreVert,
                    contentDescription = "",
                    modifier = Modifier.clickable { drop = !drop })

                DropdownMenu(expanded = drop, onDismissRequest = { drop = false}) {
                    DropdownMenuItem(text = { Text(text = "Setting") }, onClick = { })
                    DropdownMenuItem(text = { Text(text = "Messages") }, onClick = { })
                    DropdownMenuItem(text = { Text(text = "Log Out") }, onClick = {

                        Firebase.auth.signOut()

                        val sharedPreferences = context.getSharedPreferences("Linkup", Context.MODE_PRIVATE)
                        val editor = sharedPreferences.edit()
                        editor.clear()
                        editor.apply()

                        val name = sharedPreferences.getString("name", "$sharedUserName")
                        val userId = sharedPreferences.getString("userId", "$sharedUserId")

                        navController.navigate(Screen.login.route) {
                            popUpTo(navController.graph.startDestinationId) {
                                inclusive = true
                            }
                            launchSingleTop = true
                        }
                    })



                }
            }
        )
    }) {
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    top = it.calculateTopPadding(),
                    start = 10.dp,
                    end = 10.dp,
                    bottom = it.calculateBottomPadding()
                )
        ) {
            items(filteredUsers) { user ->
                UserCard(user, navController)
            }
        }
    }
}

@Composable
fun UserCard(user: User, navController: NavController) {
    val context = LocalContext.current
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                val intent = Intent(context, ChatDetail::class.java).apply {
                    putExtra("USER_ID", user.id)
                    putExtra("USERNAME", user.usernames)
                }
                context.startActivity(intent)
            }
            .padding(8.dp)
            .height(55.dp),
        colors = CardDefaults.cardColors(Color.White),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
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
            Spacer(modifier = Modifier.width(12.dp))
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.SpaceAround
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 5.dp, end = 5.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = user.usernames ?: "",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.W400
                    )
                    Text(text = "2/3/24")
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 5.dp, end = 5.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = "ok", fontSize = 14.sp)
                    Icon(
                        imageVector = Icons.Default.DoneAll,
                        contentDescription = "",
                        tint = Color.Blue,
                        modifier = Modifier.size(15.dp)
                    )
                }
            }
        }
    }
}


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun NavEntry() {
    val navHostController = rememberNavController()

    Scaffold(bottomBar = { BottomNavigation(navController = navHostController) }) {
        Navigation(navController = navHostController)
    }
}

@Composable
fun BottomNavigation(navController: NavController) {
    val items = listOf(
        Screen.Chat,
        Screen.Status,
        Screen.Call
    )

    NavigationBar {
        items.forEach { screen ->
            val navBackStackEntry by navController.currentBackStackEntryAsState()
            val currentRoute = navBackStackEntry?.destination?.route

            NavigationBarItem(
                selected = currentRoute == screen.route,
                onClick = {
                    navController.navigate(screen.route) {
                        popUpTo(navController.graph.startDestinationId) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                icon = {
                    Icon(
                        imageVector = if (currentRoute == screen.route) screen.selectedIcon else screen.unSelected,
                        contentDescription = null,
                        tint = if (currentRoute == screen.route) Color.Red else Color.Black
                    )
                },
                label = {
                    AnimatedVisibility(visible = currentRoute == screen.route) {
                        Text(text = screen.tittle, color = Color.Red)
                    }
                }
            )
        }
    }
}