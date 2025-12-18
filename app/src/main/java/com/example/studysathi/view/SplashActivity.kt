package com.example.studysathi.view

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.studysathi.R
import com.example.studysathi.ui.theme.Blue
import com.example.studysathi.ui.theme.Lavendar
import com.example.studysathi.ui.theme.PaleBlue
import com.example.studysathi.ui.theme.White0
import com.example.studysathi.view.ui.theme.StudySathiTheme
import kotlinx.coroutines.delay

class SplashActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SplashBody()
        }
    }
}

@Composable

fun SplashBody() {
    val context = LocalContext.current
    val activity = context as Activity

    // State for fade-in animation
    var visible by remember { mutableStateOf(false) }
    val alphaAnim by animateFloatAsState(if (visible) 1f else 0f,
        animationSpec = tween (
            durationMillis = 2000
        )
    )

    LaunchedEffect (Unit) {

        visible = true
        // Keep splash for 3.5 seconds (slow + stay)
        delay(3500)
        val intent = Intent(context, LoginActicity::class.java)
        context.startActivity(intent)
        activity.finish()
    }

    val gradientBrush = Brush.verticalGradient(
        colors = listOf(
            (PaleBlue),
            (Lavendar)
        )
    )

    Scaffold { padding ->
        Column (
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(brush = gradientBrush),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Image(
                painter = painterResource(R.drawable.studysathi_2), // replace with your logo
                contentDescription = "StudySathi Logo",
                modifier = Modifier
                    .size(350.dp)
                    .alpha(alphaAnim) // fade-in effect
            )
            Spacer(modifier = Modifier.height(10.dp))
            CircularProgressIndicator(color = Blue, modifier = Modifier.alpha(alphaAnim))
        }
    }
}


@Preview(showBackground = true)
@Composable
fun SplashPreview() {
    SplashBody()
}