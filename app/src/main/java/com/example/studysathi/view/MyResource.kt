package com.example.studysathi.view

import android.se.omapi.Session
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.studysathi.model.MaterialModel
import com.example.studysathi.repository.MaterialRepoImpl
import com.example.studysathi.utlis.SessionManager
import com.example.studysathi.viewmodel.MaterialViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyResource(onMaterialClick: (MaterialModel) -> Unit){

    val context = LocalContext.current
    val currentUser = SessionManager.currentUser
    val materialViewModel = remember { MaterialViewModel(MaterialRepoImpl()) }

    val allMaterials by materialViewModel.materials.collectAsState(initial = emptyList())
    val status by materialViewModel.status.collectAsState()

    LaunchedEffect(Unit) {
        materialViewModel.fetchAllMaterials()
    }

    // Toast when status changes
    LaunchedEffect(status) {
        status?.let {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
            materialViewModel.clearStatus()
        }
    }

    val myMaterials = allMaterials.filter { it.uploadedById == currentUser?.id }
    val sortedMaterials = myMaterials.reversed()

    var currentPage by remember { mutableStateOf(0) }
    val itemsPerPage = 6
    val totalPages = (sortedMaterials.size + itemsPerPage - 1) / itemsPerPage
    val pagedItems = sortedMaterials.drop(currentPage * itemsPerPage).take(itemsPerPage)

    var materialToEdit by remember { mutableStateOf<MaterialModel?>(null) }
    var materialToDelete by remember { mutableStateOf<MaterialModel?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFE3F2FD))
    ) {

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

        Surface (
            modifier = Modifier.fillMaxSize(),
            color = Color.White,
            shape = RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp),
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

                    LazyColumn(
                        modifier = Modifier
                            .weight(1f)
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(pagedItems) { material ->
                            MaterialCard(
                                material = material,
                                showActions = true,
                                onEdit = { materialToEdit = material },
                                onDelete = { materialToDelete = material },
                                onClick = { onMaterialClick(material) } // <-- Add this line
                            )
                        }
                    }

                    Row (
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        TextButton(
                            onClick = { if (currentPage > 0) currentPage-- },
                            enabled = currentPage > 0
                        ) { Text("Prev") }

                        Text("Page ${currentPage + 1} / $totalPages")

                        TextButton(
                            onClick = { if (currentPage < totalPages - 1) currentPage++ },
                            enabled = currentPage < totalPages - 1
                        ) { Text("Next") }
                    }
                }
            }
        }

        //Delete
        materialToDelete?.let { material ->
            AlertDialog(
                onDismissRequest = { materialToDelete = null },
                title = { Text("Delete Material") },
                text = { Text("Are you sure you want to delete \"${material.title}\"?") },
                confirmButton = {
                    TextButton(
                        onClick = {
                            materialViewModel.deleteMaterial(material.id)
                            materialToDelete = null
                        }
                    ) { Text("Delete") }
                },
                dismissButton = {
                    TextButton(onClick = { materialToDelete = null }) { Text("Cancel") }
                }
            )
        }

       //EDIT DIALOGUE
        materialToEdit?.let { material ->

            var title by remember { mutableStateOf(material.title) }
            var description by remember { mutableStateOf(material.description) }
            var stream by remember { mutableStateOf(material.stream) }

            val streams = listOf(
                "Science",
                "Management",
                "Humanities",
                "Engineering",
                "Medical",
                "Law",
                "Other"
            )
            var expanded by remember { mutableStateOf(false) }

            AlertDialog(
                onDismissRequest = { materialToEdit = null },
                title = { Text("Edit Material") },
                text = {
                    Column {

                        OutlinedTextField(
                            value = title,
                            onValueChange = { title = it },
                            label = { Text("Title") }
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        OutlinedTextField(
                            value = description,
                            onValueChange = { description = it },
                            label = { Text("Description") }
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        ExposedDropdownMenuBox(
                            expanded = expanded,
                            onExpandedChange = { expanded = !expanded }
                        ) {
                            OutlinedTextField(
                                value = stream,
                                onValueChange = {},
                                readOnly = true,
                                label = { Text("Stream") },
                                modifier = Modifier
                                    .menuAnchor()
                                    .fillMaxWidth(),
                                trailingIcon = {
                                    ExposedDropdownMenuDefaults.TrailingIcon(expanded)
                                }
                            )

                            ExposedDropdownMenu(
                                expanded = expanded,
                                onDismissRequest = { expanded = false }
                            ) {
                                streams.forEach { selectedStream ->
                                    DropdownMenuItem(
                                        text = { Text(selectedStream) },
                                        onClick = {
                                            stream = selectedStream
                                            expanded = false
                                        }
                                    )
                                }
                            }
                        }
                    }
                },
                confirmButton = {
                    TextButton(
                        onClick = {
                            materialViewModel.updateMaterial(
                                id = material.id,
                                title = title,
                                stream = stream,
                                description = description
                            )
                            materialToEdit = null
                        }
                    ) {
                        Text("Save")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { materialToEdit = null }) {
                        Text("Cancel")
                    }
                }
            )
        }
    }
}