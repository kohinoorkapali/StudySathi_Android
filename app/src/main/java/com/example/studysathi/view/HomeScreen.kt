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
fun HomeScreen(onMaterialClick: (MaterialModel) -> Unit) {
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
            .background(Color.White) // Cleaner base color
    ) {
        // --- PREMIMUM HEADER ---
        Surface(
            color = Color(0xFFE3F2FD),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(horizontal = 24.dp, vertical = 24.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.studysathi_2),
                    contentDescription = "Logo",
                    modifier = Modifier.height(35.dp),
                    contentScale = ContentScale.Fit
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Welcome, $userName",
                    style = MaterialTheme.typography.headlineSmall.copy(
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF0D47A1)
                    )
                )
                Text(
                    text = "What would you like to learn today?",
                    style = MaterialTheme.typography.bodyMedium.copy(color = Color(0xFF1565C0))
                )
            }
        }

        // --- CONTENT AREA ---
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 18.dp)
        ) {
            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Latest Resources",
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.ExtraBold,
                    color = Color(0xFF1E293B)
                )
            )

            Text(
                text = "Recently uploaded by your peers",
                style = MaterialTheme.typography.bodySmall.copy(color = Color.Gray),
                modifier = Modifier.padding(bottom = 16.dp)
            )

            val newestMaterials = materials.takeLast(9).reversed()

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(14.dp),
                contentPadding = PaddingValues(bottom = 100.dp) // Space for FAB
            ) {
                items(newestMaterials) { material ->
                    // This calls your external MaterialCard component
                    MaterialCard(
                        material = material,
                        onClick = { onMaterialClick(material) }
                    )
                }
            }
        }
    }
}


