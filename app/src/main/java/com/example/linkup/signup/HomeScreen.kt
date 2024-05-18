import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PersonPin
import androidx.compose.material.icons.filled.Search
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
import com.example.linkup.data.remote.Repository
import com.example.linkup.navigation.Navigation
import com.example.linkup.navigation.Screen
import com.google.firebase.Firebase
import com.google.firebase.database.database
import com.google.firebase.database.getValue
@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun HomeScreen(navController: NavController, sharedUserId: String?) {

    var search by remember {
        mutableStateOf("")
    }
    val dbRef = Firebase.database.getReference("Users")
    val repository = remember {
        Repository(dbRef)
    }
    val context = LocalContext.current

    var data by remember { mutableStateOf<User?>(null) }
    println("SHARED $sharedUserId")
    LaunchedEffect(key1 = Unit) {
        val myRef = dbRef.database.getReference("Users")

        myRef.child(sharedUserId.toString()).get().addOnSuccessListener {
            val user = it.getValue<User>()
            println("USER: $user")

            data = user

        }

    }
    Scaffold(topBar = {
        LargeTopAppBar(title = {
            TextField(
                value = search,
                onValueChange = {
                    search = it
                },
                placeholder = {
                    Text(text = "Search")
                },
                trailingIcon = {
                    Icon(imageVector = Icons.Default.Search, contentDescription = "")
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
                    focusedIndicatorColor = Color.Transparent, unfocusedIndicatorColor = Color.Transparent
                )
            )

        }, navigationIcon = {
            Text(text = "Link Up", fontSize = 22.sp, fontWeight = FontWeight.SemiBold)
        })
    }) {
        Text(text = "This is Home Screen")
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
    val item = listOf(
        Screen.Home,
        Screen.Status,
        Screen.Call
    )

    NavigationBar {
        item?.forEach {
            val navStack by navController.currentBackStackEntryAsState()
            val current = navStack?.destination?.route

            NavigationBarItem(selected = current == it.route, onClick = {
                navController.navigate(it.route) {
                    navController?.graph.let {
                        it?.route?.let { it1 -> popUpTo(it1) }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            }, icon = {
                if (current == it.route) {
                    Icon(
                        imageVector = it.selectedIcon,
                        contentDescription = "",
                        tint = Color.Red
                    )
                } else {
                    Icon(imageVector = it.unSelected, contentDescription = "")
                }
            },

                label = {
                    if (current == it.route) {
                        Text(text = it.tittle, color = Color.Red)
                    } else {
                        Text(text = it.tittle, color = Color.White)
                    }
                })
        }
    }
}
