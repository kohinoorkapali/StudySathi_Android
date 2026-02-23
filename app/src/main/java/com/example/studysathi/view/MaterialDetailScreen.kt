package com.example.studysathi.view

import android.app.DownloadManager
import android.content.Context
import android.net.Uri
import android.os.Environment
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Download
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
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
    val context = LocalContext.current

    Scaffold(
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
                onClick = {
                    if (material.fileUrl.isNotEmpty()) {
                        downloadFile(context, material.fileUrl, material.fileName)
                    } else {
                        Toast.makeText(context, "No file to download", Toast.LENGTH_SHORT).show()
                    }
                },
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
                .background(Color(0xFFF5F9FF))
                .padding(paddingValues)
                .padding(20.dp)
        ) {
            // Header Section
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

            // Description
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

// --- DOWNLOAD HELPER ---
fun downloadFile(context: Context, url: String, fileName: String) {
    try {
        val safeFileName = if (!fileName.endsWith(".pdf")) "$fileName.pdf" else fileName
        // 👇 LOG THE FULL URL HERE
        Log.d("DownloadURL", "URL: $url")

        val request = DownloadManager.Request(Uri.parse(url))
            .setTitle(safeFileName)
            .setDescription("Downloading $safeFileName")
            .setNotificationVisibility(
                DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED
            )
            .setAllowedOverMetered(true)
            .setAllowedOverRoaming(true)

        request.setDestinationInExternalFilesDir(
            context,
            Environment.DIRECTORY_DOWNLOADS,
            safeFileName
        )

        val manager = context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
        val downloadId = manager.enqueue(request)

        Log.d("DownloadID", "Enqueued with ID: $downloadId")

        Toast.makeText(context, "Downloading $safeFileName", Toast.LENGTH_SHORT).show()

    } catch (e: Exception) {
        e.printStackTrace()
        Log.e("DownloadError", "Error: ${e.message}")
        Toast.makeText(context, "Download failed: ${e.message}", Toast.LENGTH_LONG).show()
    }
}