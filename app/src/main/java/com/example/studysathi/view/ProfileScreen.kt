package com.example.studysathi.view
import android.content.Intent
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
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

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFE3F2FD))
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(24.dp))

        Image(
            painter = painterResource(R.drawable.baseline_person_24),
            contentDescription = "Profile Image",
            modifier = Modifier.size(100.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "My Profile",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF0D47A1)
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Full Name
        OutlinedTextField(
            value = fullName,
            onValueChange = { fullName = it },
            label = { Text("Full Name") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            enabled = isEditing
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Username
        OutlinedTextField(
            value = username,
            onValueChange = { username = it },
            label = { Text("Username") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            enabled = isEditing
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Email
        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            enabled = isEditing
        )

        Spacer(modifier = Modifier.height(24.dp))

        if (!isEditing) {
            Button(
                onClick = { isEditing = true },
                modifier = Modifier.fillMaxWidth().height(50.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0D47A1))
            ) { Text("Edit Profile", color = Color.White) }
        } else {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Button(
                    onClick = {
                        currentUser?.let { user ->
                            val updatedUser = user.copy(
                                fullName = fullName,
                                username = username,
                                email = email
                            )

                            // Save to Firebase
                            userViewModel.editProfile(user.id, updatedUser) { success, message ->
                                if (success) {
                                    // Update local session
                                    SessionManager.currentUser = updatedUser

                                    // Update materials uploaded by user
                                    materialViewModel.updateAllMaterialsUsername(user.id, username)

                                    Toast.makeText(context, "Profile updated successfully", Toast.LENGTH_SHORT).show()
                                    isEditing = false
                                } else {
                                    Toast.makeText(context, "Update failed: $message", Toast.LENGTH_SHORT).show()
                                }
                            }
                        }
                    },
                    modifier = Modifier.weight(1f).height(50.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0D47A1))
                ) { Text("Update", color = Color.White) }

                Button(
                    onClick = {
                        fullName = currentUser?.fullName ?: ""
                        username = currentUser?.username ?: ""
                        email = currentUser?.email ?: ""
                        isEditing = false
                    },
                    modifier = Modifier.weight(1f).height(50.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Gray)
                ) { Text("Cancel", color = Color.White) }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                // Clear session
                SessionManager.currentUser = null

                // Navigate to LoginActivity
                val intent = Intent(context, LoginActicity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                context.startActivity(intent)
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
        ) {
            Text("Logout", color = Color.White)
        }
    }
}