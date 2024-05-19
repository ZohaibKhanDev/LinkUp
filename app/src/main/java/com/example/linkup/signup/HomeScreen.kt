import android.annotation.SuppressLint
import android.content.Intent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DoneAll
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
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
import com.google.firebase.database.*


@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun HomeScreen(navController: NavController, sharedUserId: String?) {

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
                Text(text = "Link Up", fontSize = 22.sp, fontWeight = FontWeight.SemiBold)
            }
        )
    }) {
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = it.calculateTopPadding(), start = 10.dp, end = 10.dp)
        ) {
            items(users) { user ->
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
                    Text(text = "Ok", fontSize = 14.sp)
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
