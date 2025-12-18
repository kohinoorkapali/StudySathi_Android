package com.example.studysathi.view

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.studysathi.DashboardActivity
import com.example.studysathi.R
import com.example.studysathi.repository.UserRepoImpl

import com.example.studysathi.ui.theme.Blue
import com.example.studysathi.ui.theme.BluishWHite
import com.example.studysathi.ui.theme.White0
import com.example.studysathi.viewmodel.UserViewModel

class ForgotActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ForgotBody()
        }
    }
}

@Composable
fun ForgotBody() {
    var email by remember { mutableStateOf("") }
    var loading by remember { mutableStateOf(false) }

    val context = LocalContext.current
    val userViewModel = remember { UserViewModel(UserRepoImpl()) }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(White0),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item { Spacer(modifier = Modifier.height(150.dp)) }
        item {
            Image(
                painter = painterResource(id = R.drawable.studysathi_2), // replace with your logo file
                contentDescription = "App Logo",
                modifier = Modifier
                    .height(100.dp)
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            )
        }

        item { Spacer(modifier = Modifier.height(50.dp)) }

        item {
            Text(
                text = "Forgot Password",
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold,
                color = Blue
            )
        }

        item { Spacer(modifier = Modifier.height(20.dp)) }

        item {
            Text(
                text = "Enter your registered email to receive a password reset link.",
                fontSize = 14.sp,
                color = Color.Gray,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
        }

        item { Spacer(modifier = Modifier.height(30.dp)) }

        item {
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                placeholder = { Text("Email") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                shape = RoundedCornerShape(12.dp),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = BluishWHite,
                    unfocusedContainerColor = BluishWHite,
                    focusedIndicatorColor = Blue,
                    unfocusedIndicatorColor = Color.Transparent
                )
            )
        }

        item { Spacer(modifier = Modifier.height(28.dp)) }

        item {
            Button(
                onClick = {
                    if (email.isBlank()) {
                        Toast.makeText(context, "Please enter email", Toast.LENGTH_SHORT).show()
                        return@Button
                    }

                    loading = true

                    userViewModel.forgetPassword(email) { success, message ->
                        loading = false
                        Toast.makeText(context, message, Toast.LENGTH_LONG).show()
                        if (success) {
                            // Navigate back to Login after sending reset email
                            val intent = Intent(context, LoginActicity::class.java)
                            context.startActivity(intent)
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .height(52.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Blue),
                enabled = !loading
            ) {
                Text(text = if (loading) "Sending..." else "Reset Password", color = Color.White)
            }
        }

        item {
            Spacer(modifier = Modifier.height(20.dp))
            Text(
                text = "Back to Login",
                color = Blue,
                modifier = Modifier
                    .clickable {
                        val intent = Intent(context, LoginActicity::class.java)
                        context.startActivity(intent)
                    }
                    .padding(16.dp)
            )
        }
    }
}


@Preview(showBackground = true)
@Composable
fun ForgotPreview() {
    ForgotBody()
}