import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.linkup.User
import com.example.linkup.data.remote.MainViewModel
import com.example.linkup.data.remote.Repository
import com.example.linkup.navigation.Navigation
import com.example.linkup.navigation.Screen1
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.database.database
import com.google.firebase.database.getValue

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun HomeScreen(navController: NavController, sharedUserId: String?) {

    val dbRef = Firebase.database.getReference("Users")
    val repository = remember {
        Repository(dbRef)
    }
    val context = LocalContext.current
    val viewModel = remember {
        MainViewModel(repository)
    }
    val userId = Firebase.auth.currentUser?.uid ?: ""
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

    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center){
        Text(text = "This is Home Screen")
    }
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun NavEntry() {
    val navHostController= rememberNavController()

    Scaffold (bottomBar = { BottomNavigation(navController = navHostController)}){
        Navigation(navController = navHostController)

    }
}
@Composable
fun BottomNavigation(navController: NavController) {
    val item= listOf(
        Screen1.Home,
        Screen1.Status,
        Screen1.Call
    )

    NavigationBar {
        item?.forEach {
            val navStack by navController.currentBackStackEntryAsState()
            val current=navStack?.destination?.route

            NavigationBarItem(selected = current==it.route, onClick = {
                navController.navigate(it.route) {
                    navController?.graph.let {
                        it?.route?.let { it1 -> popUpTo(it1) }
                        launchSingleTop = true
                        restoreState = true
                    }
                } }, icon = {
                if (current==it.route){
                    Icon(imageVector = it.selectedIcon, contentDescription = "", tint = Color.Red)
                }
                else{
                    Icon(imageVector = it.unSelected, contentDescription ="",  )
                }
            },

                label = {
                    if (current==it.route){
                        Text(text = it.tittle, color = Color.Red)
                    }
                    else{
                        Text(text = it.tittle, color = Color.White)
                    }
                })
        }
    }
}