package com.example.studysathi.view

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.studysathi.DashboardActivity
import com.example.studysathi.R
import com.example.studysathi.repository.UserRepoImpl
import com.example.studysathi.ui.theme.Blue
import com.example.studysathi.ui.theme.BluishWHite
import com.example.studysathi.ui.theme.White0
import com.example.studysathi.view.ui.theme.StudySathiTheme

class LoginActicity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            LoginBody()
        }
    }
}

@Composable
fun LoginBody(){

    val userRepo = remember { UserRepoImpl() }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var visibility by remember { mutableStateOf(false) }

    val context = LocalContext.current
    val activity = context as Activity

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
                text = "Login",
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold,
                color = Blue
            )
        }

        item { Spacer(modifier = Modifier.height(16.dp)) }

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

        item { Spacer(modifier = Modifier.height(16.dp)) }

        item {
            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                placeholder = { Text("Password") },
                visualTransformation = if (visibility) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    IconButton(onClick = { visibility = !visibility }) {
                        Icon(
                            painter = if (visibility)
                                painterResource(id = R.drawable.baseline_visibility_off_24)
                            else
                                painterResource(id = R.drawable.baseline_visibility_24),
                            contentDescription = null
                        )
                    }
                },
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

        item {
            Spacer(modifier = Modifier.height(16.dp))
            Row {
                Text(
                    text = "Forgot password??",
                    color = Blue,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.clickable {

                        val intent = Intent(context, ForgotActivity::class.java)
                        context.startActivity(intent)
                        activity.finish()
                    }
                )
            }
        }

        // Login Button
        item {
            Spacer(modifier = Modifier.height(28.dp))
            Button(
                onClick = {
                    if (email.isEmpty() || password.isEmpty()) {
                        Toast.makeText(context, "Email and Password required", Toast.LENGTH_SHORT).show()
                        return@Button
                    }

                    // Call Firebase login
                    userRepo.login(email, password) { success, message ->
                        if (success) {
                            Toast.makeText(context, "Login Successful", Toast.LENGTH_SHORT).show()
                            val intent = Intent(context, DashboardActivity::class.java)
                            context.startActivity(intent)
                            activity.finish()
                        } else {
                            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                        }
                    }

                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .height(52.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Blue)
            ) {
                Text("Login", color = Color.White)
            }
        }


        item {
            Spacer(modifier = Modifier.height(16.dp))
            Row {
                Text("Don't have an account? ")
                Text(
                    text = "Sign Up",
                    color = Blue,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.clickable {

                        val intent = Intent(context, RegistrationActivity::class.java)
                        context.startActivity(intent)
                        activity.finish()
                    }
                )
            }
        }
    }
}
@Preview(showBackground = true)
@Composable
fun LoginPreview(){
    LoginBody()
}