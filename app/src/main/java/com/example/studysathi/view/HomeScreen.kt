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
import androidx.compose.material3.Icon
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
            .background(Color(0xFFF8FAFF))
    ) {
        // --- REFINED HEADER ---
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFF81D4FA))
                .padding(start = 20.dp, end = 20.dp, top = 12.dp, bottom = 12.dp) // Minimal top padding
        ) {
            Image(
                painter = painterResource(id = R.drawable.studysathi_2),
                contentDescription = "StudySathi Logo",
                modifier = Modifier
                    .width(120.dp)
                    .wrapContentHeight(),
                contentScale = ContentScale.Fit,
                alignment = Alignment.TopStart
            )

            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = "Welcome to Study Sathi",
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.ExtraBold,
                    color = Color(0xFF1976D2)
                )
            )
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp)
                .background(Color(0xFFE3F2FD))
        ) {
            Spacer(modifier = Modifier.height(16.dp))
            0xFFE3F2FD

            Text(
                text = "Latest Resources",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1E293B)
                )
            )

            Text(
                text = "Recently uploaded study materials",
                style = MaterialTheme.typography.bodySmall.copy(color = Color.Gray),
                modifier = Modifier.padding(bottom = 12.dp)
            )

            val newestMaterials = materials.takeLast(9).reversed()

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = PaddingValues(bottom = 80.dp) // Padding for bottom nav bar
            ) {
                items(newestMaterials) { material ->
                    MaterialCard(material)
                }
            }
        }
    }
}


