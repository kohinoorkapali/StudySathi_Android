package com.example.studysathi.view
import android.content.Intent
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.studysathi.DashboardActivity
import com.example.studysathi.R
import com.example.studysathi.repository.MaterialRepoImpl
import com.example.studysathi.repository.UserRepoImpl
import com.example.studysathi.utlis.SessionManager
import com.example.studysathi.viewmodel.MaterialViewModel
import com.example.studysathi.viewmodel.UserViewModel


@Composable
fun ProfileScreen() {
    val context = LocalContext.current
    val currentUser = SessionManager.currentUser
    val userViewModel = remember { UserViewModel(UserRepoImpl()) }
    val materialViewModel = remember { MaterialViewModel(MaterialRepoImpl()) }

    var fullName by remember { mutableStateOf(currentUser?.fullName ?: "") }
    var username by remember { mutableStateOf(currentUser?.username ?: "") }
    var email by remember { mutableStateOf(currentUser?.email ?: "") }
    var isEditing by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFE3F2FD)) // Matches your MyResource background
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // --- TOP SECTION (Avatar & Title) ---
            Spacer(modifier = Modifier.height(40.dp))

            Surface(
                shape = RoundedCornerShape(50.dp),
                color = Color.White,
                shadowElevation = 4.dp,
                modifier = Modifier.size(100.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Image(
                        painter = painterResource(R.drawable.baseline_person_24),
                        contentDescription = "Profile Image",
                        modifier = Modifier.size(60.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = if (isEditing) "Edit Your Profile" else fullName,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF0D47A1)
            )

            Text(
                text = "@$username",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Gray
            )

            Spacer(modifier = Modifier.height(24.dp))

            // --- INFO CARD ---
            Surface(
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1f),
                color = Color.White,
                shape = RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp),
                shadowElevation = 8.dp
            ) {
                Column(
                    modifier = Modifier
                        .padding(24.dp)
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text(
                        text = "Account Information",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF1E293B)
                    )

                    ProfileTextField(
                        value = fullName,
                        onValueChange = { fullName = it },
                        label = "Full Name",
                        enabled = isEditing
                    )

                    ProfileTextField(
                        value = username,
                        onValueChange = { username = it },
                        label = "Username",
                        enabled = isEditing
                    )

                    ProfileTextField(
                        value = email,
                        onValueChange = { email = it },
                        label = "Email Address",
                        enabled = isEditing
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    // --- BUTTONS ---
                    if (!isEditing) {
                        Button(
                            onClick = { isEditing = true },
                            modifier = Modifier.fillMaxWidth().height(56.dp),
                            shape = RoundedCornerShape(16.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0D47A1))
                        ) {
                            Text("Edit Profile", fontWeight = FontWeight.Bold)
                        }
                    } else {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            OutlinedButton(
                                onClick = {
                                    fullName = currentUser?.fullName ?: ""
                                    username = currentUser?.username ?: ""
                                    email = currentUser?.email ?: ""
                                    isEditing = false
                                },
                                modifier = Modifier.weight(1f).height(56.dp),
                                shape = RoundedCornerShape(16.dp)
                            ) {
                                Text("Cancel")
                            }

                            Button(
                                onClick = {
                                    currentUser?.let { user ->
                                        val updatedUser = user.copy(fullName = fullName, username = username, email = email)
                                        userViewModel.editProfile(user.id, updatedUser) { success, message ->
                                            if (success) {
                                                SessionManager.currentUser = updatedUser
                                                materialViewModel.updateAllMaterialsUsername(user.id, username)
                                                Toast.makeText(context, "Profile Updated", Toast.LENGTH_SHORT).show()
                                                isEditing = false
                                            }
                                        }
                                    }
                                },
                                modifier = Modifier.weight(1f).height(56.dp),
                                shape = RoundedCornerShape(16.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50))
                            ) {
                                Text("Save", fontWeight = FontWeight.Bold)
                            }
                        }
                    }

                    // Logout stays at the bottom or separate
                    TextButton(
                        onClick = {
                            SessionManager.currentUser = null
                            val intent = Intent(context, LoginActicity::class.java)
                            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                            context.startActivity(intent)
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Logout from Account", color = Color.Red)
                    }
                }
            }
        }
    }
}

// Reusable component to keep code clean
@Composable
fun ProfileTextField(value: String, onValueChange: (String) -> Unit, label: String, enabled: Boolean) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        singleLine = true,
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        enabled = enabled,
        colors = OutlinedTextFieldDefaults.colors(
            disabledBorderColor = Color(0xFFEEEEEE),
            disabledLabelColor = Color.Gray,
            disabledTextColor = Color.Black
        )
    )
}