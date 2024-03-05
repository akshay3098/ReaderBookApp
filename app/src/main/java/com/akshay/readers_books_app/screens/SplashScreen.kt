package com.akshay.readers_books_app.screens

import android.view.animation.OvershootInterpolator
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.animation.core.Animatable
import androidx.compose.ui.draw.scale
import androidx.navigation.NavController
import com.akshay.readers_books_app.components.ReaderLogo
import com.akshay.readers_books_app.navigation.ReaderScreens
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.delay

@Composable
fun splashScreen(navController: NavController = NavController(context = LocalContext.current)) {

    val scale = remember {
        Animatable(0f)
    }

    LaunchedEffect(key1 = true) {
        scale.animateTo(targetValue = 0.9f, animationSpec = tween(durationMillis = 1000, easing = {
            OvershootInterpolator(8f).getInterpolation(it)
        }))
        delay(1000L)
        if (FirebaseAuth.getInstance().currentUser?.email.isNullOrEmpty()) {
            navController.navigate(ReaderScreens.HomeScreen.name)
        } else {
            navController.navigate(ReaderScreens.LoginScreen.name)
        }

    }



    Surface(modifier = Modifier
        .padding(4.dp)
        .size(330.dp).scale(scale.value), shape = CircleShape, border = BorderStroke(2.dp,
        Color.Yellow)) {
        Column(verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
            ReaderLogo()
            Spacer(modifier = Modifier.height(45.dp))
            Text(text = "Read. Change. Yourself", fontSize = 20.sp, color = Color.LightGray.copy(alpha = 0.5f))
        }
    }

}
