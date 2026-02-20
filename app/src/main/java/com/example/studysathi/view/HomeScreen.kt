package com.example.studysathi.view

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil3.compose.rememberAsyncImagePainter
import com.example.studysathi.R
import com.example.studysathi.model.MaterialModel
import com.example.studysathi.viewmodel.MaterialViewModel
import com.example.studysathi.repository.MaterialRepoImpl
import com.example.studysathi.utlis.SessionManager

@Composable
fun HomeScreen() {
    val currentUser = SessionManager.currentUser
    val userName = currentUser?.fullName ?: "Student"
    val materialViewModel = remember { MaterialViewModel(MaterialRepoImpl()) }
    val materials by materialViewModel.materials.collectAsState()

    LaunchedEffect(Unit) {
        materialViewModel.fetchAllMaterials()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFE3F2FD))
    ) {
        Column(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .padding(top = 0.dp, bottom = 16.dp)
        ) {
            Image(
                painter = painterResource(R.drawable.studysathi_2),
                contentDescription = "App Logo",
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .height(80.dp)
                    .wrapContentWidth()
                    .offset(x = (-12).dp, y = (-5).dp)
            )

            Text(
                text = "Hello, $userName 👋",
                style = MaterialTheme.typography.headlineMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF0D47A1)
                )
            )
            Text(
                text = "Ready to study today?",
                style = MaterialTheme.typography.bodyMedium.copy(color = Color(0xFF1976D2))
            )
        }
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = Color.White, // This makes the list area white
            shape = RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp),
            shadowElevation = 8.dp
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Newest Uploads",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                val newestMaterials = materials.takeLast(9).reversed()

                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    contentPadding = PaddingValues(bottom = 24.dp)
                ) {
                    items(newestMaterials) { material ->
                        MaterialCard(material)
                    }
                }
            }
        }
    }
}

