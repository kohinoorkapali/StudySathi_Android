package com.example.studysathi.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Download
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.studysathi.model.MaterialModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MaterialDetailScreen(
    material: MaterialModel,
    onBack: () -> Unit
) {
    Scaffold (
        topBar = {
            TopAppBar(
                title = { Text("Material Details", fontWeight = FontWeight.SemiBold) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White,
                    titleContentColor = Color(0xFF0D47A1)
                )
            )
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = { /* Handle Download */ },
                containerColor = Color(0xFF0D47A1),
                contentColor = Color.White,
                icon = { Icon(Icons.Default.Download, contentDescription = null) },
                text = { Text("Download") }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF5F9FF)) // Softer background
                .padding(paddingValues)
                .padding(20.dp)
        ) {
            // Header Section with Icon
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                shape = RoundedCornerShape(16.dp)
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Column(modifier = Modifier.padding(start = 16.dp)) {
                        Text(
                            text = material.title,
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF1A1C1E)
                        )
                        Text(
                            text = material.stream,
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.Gray
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Details Section
            Text(
                text = "Description",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF0D47A1)
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = material.description,
                style = MaterialTheme.typography.bodyLarge,
                lineHeight = 24.sp,
                color = Color.DarkGray
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Metadata Row
            Divider(color = Color.LightGray.copy(alpha = 0.5f))

            Spacer(modifier = Modifier.height(16.dp))

            InfoRow(label = "Uploaded by", value = material.uploadedBy)
            InfoRow(label = "File Name", value = material.fileName)
        }
    }
}

@Composable
fun InfoRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = label, color = Color.Gray, style = MaterialTheme.typography.bodyMedium)
        Text(text = value, fontWeight = FontWeight.Medium, style = MaterialTheme.typography.bodyMedium)
    }
}