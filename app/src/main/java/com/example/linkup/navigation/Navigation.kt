package com.example.linkup.navigation

import CallsScreen
import HomeScreen
import android.content.Context
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Upcoming
import androidx.compose.material.icons.outlined.Call
import androidx.compose.material.icons.outlined.Chat
import androidx.compose.material.icons.outlined.Upcoming
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.linkup.BottomNavigation.StatusScreen
import com.example.linkup.signup.AuthScreen
import com.example.linkup.signup.LoginScreen
import com.example.linkup.signup.SignUpScreen

@Composable
fun Navigation(navController: NavHostController) {

    val context = LocalContext.current
    val sharedPreferences = context.getSharedPreferences("Linkup", Context.MODE_PRIVATE)
    val sharedUserId = sharedPreferences.getString("userId", null)
    val destination = remember {
        if (sharedUserId == null) {
            Screen.Main.route
        } else {
            Screen.Chat.route
        }
    }

    NavHost(navController = navController, startDestination =destination) {
        composable(Screen.Main.route) {
            AuthScreen(navController)
        }

        composable(Screen.signup.route) {
            SignUpScreen(navController)
        }

        composable(Screen.login.route) {
            LoginScreen(navController)
        }
        composable(Screen.Chat.route) {

            HomeScreen(navController, sharedUserId)
        }

        composable(Screen.Status.route) {
            StatusScreen(navController)
        }

        composable(Screen.Call.route) {
            CallsScreen(navController)
        }

    }
}

sealed class Screen(
    val route: String,
    val tittle: String,
    val selectedIcon: ImageVector,
    val unSelected: ImageVector
) {
    object Main :
        Screen(
            "Main",
            "Main", selectedIcon = Icons.Default.Star, unSelected = Icons.Default.Star
        )

    object signup : Screen(
        "signup", "signUp", selectedIcon = Icons.Default.Star, unSelected = Icons.Default.Star
    )

    object login : Screen(
        "login", "login", selectedIcon = Icons.Default.Star, unSelected = Icons.Default.Star
    )

    object Chat : Screen(
        "Chat", "Chat", selectedIcon = Icons.Default.Chat, unSelected = Icons.Outlined.Chat
    )

    object Status : Screen(
        "Status",
        "Status",
        selectedIcon = Icons.Filled.Upcoming,
        unSelected = Icons.Outlined.Upcoming
    )

    object Call : Screen(
        "Call", "Call", selectedIcon = Icons.Filled.Call, unSelected = Icons.Outlined.Call
    )


}
