package com.example.studysathi.view

import android.se.omapi.Session
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.studysathi.model.MaterialModel
import com.example.studysathi.repository.MaterialRepoImpl
import com.example.studysathi.utlis.SessionManager
import com.example.studysathi.viewmodel.MaterialViewModel

@Composable
fun MyResource() {
    val currentUser = SessionManager.currentUser
    val materialViewModel = remember { MaterialViewModel(MaterialRepoImpl()) }
    val allMaterials by materialViewModel.materials.collectAsState(initial = emptyList())

    LaunchedEffect(Unit) {
        materialViewModel.fetchAllMaterials()
    }

    val myMaterials = allMaterials.filter { it.uploadedBy == currentUser?.username }
    val sortedMaterials = myMaterials.reversed()

    // Pagination state
    var currentPage by remember { androidx.compose.runtime.mutableStateOf(0) }
    val itemsPerPage = 6
    val totalPages = (sortedMaterials.size + itemsPerPage - 1) / itemsPerPage
    val pagedItems = sortedMaterials.drop(currentPage * itemsPerPage).take(itemsPerPage)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFE3F2FD))
    ) {
        // Header
        Text(
            text = "My Resources",
            fontWeight = FontWeight.Bold,
            color = Color(0xFF0D47A1),
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp),
            textAlign = androidx.compose.ui.text.style.TextAlign.Center
        )
        Spacer(modifier = Modifier.height(16.dp))

        // White rounded background
        androidx.compose.material3.Surface(
            modifier = Modifier
                .fillMaxSize(),
            color = Color.White,
            shape = androidx.compose.foundation.shape.RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp),
            shadowElevation = 8.dp
        ) {
            Column(modifier = Modifier.fillMaxSize()) {
                if (sortedMaterials.isEmpty()) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("You haven’t uploaded any materials yet")
                    }
                } else {
                    // Material list
                    LazyColumn(
                        modifier = Modifier
                            .weight(1f)
                            .padding(horizontal = 16.dp, vertical = 16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(pagedItems) { material ->
                            MaterialCard(
                                material = material,
                                showActions = true, // This enables Edit/Delete buttons
                                onEdit = {
                                    // TODO: Navigate to edit screen or show edit dialog
                                    // Example: editMaterial(material)
                                },
                                onDelete = {
                                    // TODO: Delete the material
//                                    materialViewModel.delete(material) {
//                                        // Refresh after deletion
//                                        materialViewModel.fetchAllMaterials()
//                                    }
                                }
                            )
                        }
                    }

                    // Small pagination controls
                    androidx.compose.foundation.layout.Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        androidx.compose.material3.TextButton(
                            onClick = { if (currentPage > 0) currentPage-- },
                            enabled = currentPage > 0
                        ) {
                            Text("Prev", style = MaterialTheme.typography.bodySmall)
                        }

                        Text(
                            text = "Page ${currentPage + 1} / $totalPages",
                            style = MaterialTheme.typography.bodySmall
                        )

                        androidx.compose.material3.TextButton(
                            onClick = { if (currentPage < totalPages - 1) currentPage++ },
                            enabled = currentPage < totalPages - 1
                        ) {
                            Text("Next", style = MaterialTheme.typography.bodySmall)
                        }
                    }
                }
            }
        }
    }
}