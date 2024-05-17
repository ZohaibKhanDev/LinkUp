package com.example.linkup.signup

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.linkup.R
import com.example.linkup.navigation.Screen1

@Composable
fun AuthScreen(navController: NavController) {
    val context = LocalContext.current
    val sharedPreferences = context.getSharedPreferences("Linkup", Context.MODE_PRIVATE)
    val sharedUserId = sharedPreferences.getString("userId", null)

    LaunchedEffect(key1 = Unit) {
        if (sharedUserId != null) {
            navController.navigate(Screen1.Home.route) {
                popUpTo(Screen1.Home.route) { inclusive = true }
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.linkup),
            contentDescription = "",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(190.dp)
                .aspectRatio(1f)
                .clip(CircleShape)
        )
        Spacer(modifier = Modifier.height(25.dp))

        Text(
            text = "Link Up",
            fontSize = MaterialTheme.typography.titleLarge.fontSize,
            fontWeight = FontWeight.SemiBold
        )

        Spacer(modifier = Modifier.height(40.dp))

        Button(
            onClick = { navController.navigate(Screen1.signup.route) },
            modifier = Modifier
                .width(273.dp)
                .height(50.dp), shape = RoundedCornerShape(5.dp)
        ) {
            Text(text = "SignUp")
        }

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedButton(
            onClick = {
                navController.navigate(Screen1.login.route)
            },
            modifier = Modifier
                .width(273.dp)
                .height(50.dp),
            shape = RoundedCornerShape(5.dp)
        ) {
            Text(text = "Login")
        }

    }

}