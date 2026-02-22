package com.example.studysathi.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.studysathi.repository.MaterialRepoImpl
import com.example.studysathi.viewmodel.MaterialViewModel
import com.example.studysathi.view.MaterialCard
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.ui.Alignment
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import com.example.studysathi.R
import com.example.studysathi.model.MaterialModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(onMaterialClick: (MaterialModel) -> Unit){
    val materialViewModel = remember { MaterialViewModel(MaterialRepoImpl()) }
    val allMaterials by materialViewModel.materials.collectAsState(initial = emptyList())

    var searchQuery by remember { mutableStateOf("") }
    var expandedStream by remember { mutableStateOf(false) }
    var selectedStream by remember { mutableStateOf("All Types") }
    var sortByNewest by remember { mutableStateOf(true) }

    val streams = listOf("All Types", "Science", "Management", "Humanities", "Engineering", "Medical", "Law")

    LaunchedEffect(Unit) { materialViewModel.fetchAllMaterials() }

    // Filtering logic
    val filteredMaterials = allMaterials.filter {
        (it.title.contains(searchQuery, true) || it.uploadedBy.contains(searchQuery, true)) &&
                (selectedStream == "All Types" || it.stream == selectedStream)
    }

    // Sorting logic
    val sortedMaterials = if (sortByNewest) filteredMaterials.reversed() // Replace with .uploadedAt if available
    else filteredMaterials

    var currentPage by remember { mutableStateOf(0) }
    val itemsPerPage = 8
    val totalPages = (sortedMaterials.size + itemsPerPage - 1) / itemsPerPage
    val pagedItems = sortedMaterials.drop(currentPage * itemsPerPage).take(itemsPerPage)


    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFE3F2FD))

    ) {
        // --- 1. Header & Logo ---
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally // center contents horizontally
        ) {
            // Header Text
            Text(
                text = "Resources",
                fontWeight = FontWeight.Bold,
                color = Color(0xFF0D47A1),
                style = MaterialTheme.typography.headlineSmall
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // --- 2. Rounded Search Bar ---
        OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            placeholder = { Text("Search title or author...") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp) // Maintain side gaps
                .background(Color.White, shape = RoundedCornerShape(28.dp))
                .height(56.dp),
            shape = RoundedCornerShape(28.dp),
            singleLine = true,
            leadingIcon = {
                Icon(
                    painter = painterResource(R.drawable.baseline_search_24),
                    contentDescription = "Search",
                    tint = Color.Gray
                )
            },
            colors = androidx.compose.material3.OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color(0xFF1976D2),
                unfocusedBorderColor = Color.Transparent,
                disabledBorderColor = Color.Transparent
            )
        )

        Spacer(modifier = Modifier.height(16.dp))

        // --- 3. Filter and Sort Row (Matched to your image) ---
        Row(
            modifier = Modifier.fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)

        ) {
            // "All Types" Dropdown Filter
            Box(modifier = Modifier.weight(1f)) {
                ExposedDropdownMenuBox(
                    expanded = expandedStream,
                    onExpandedChange = { expandedStream = !expandedStream }
                ) {
                    Surface (
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(45.dp)
                            .menuAnchor(),
                        shape = RoundedCornerShape(24.dp),
                        color = Color.White,
                        shadowElevation = 2.dp
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 16.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(text = selectedStream, style = MaterialTheme.typography.bodyMedium)
                            Icon(
                                painter = painterResource(R.drawable.baseline_keyboard_arrow_down_24), // Replace with your down arrow
                                contentDescription = null,
                                modifier = Modifier.size(14.dp)
                            )
                        }
                    }
                    ExposedDropdownMenu(
                        expanded = expandedStream,
                        onDismissRequest = { expandedStream = false }
                    ) {
                        streams.forEach { stream ->
                            DropdownMenuItem(
                                text = { Text(stream) },
                                onClick = {
                                    selectedStream = stream
                                    expandedStream = false
                                }
                            )
                        }
                    }
                }
            }

            // "Sort" Toggle Button
            Surface(
                modifier = Modifier
                    .weight(0.6f)
                    .height(45.dp)
                    .padding(horizontal = 16.dp)
                    .clickable { sortByNewest = !sortByNewest },
                shape = RoundedCornerShape(24.dp),
                color = Color.White,
                shadowElevation = 2.dp
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = if (sortByNewest) "Newest" else "Oldest",
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Icon(
                        painter = painterResource(R.drawable.baseline_sort_24), // Replace with your sort icon
                        contentDescription = null,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // --- 4. Material List ---
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = Color.White,
            shape = RoundedCornerShape(
                topStart = 32.dp,
                topEnd = 32.dp
            ),
            shadowElevation = 8.dp
        ) {
            Column(
                modifier = Modifier.fillMaxSize()
            ) {
                LazyColumn(
                    modifier = Modifier.weight(1f),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(pagedItems) { material ->
                        MaterialCard(
                            material = material,
                            onClick = { onMaterialClick(material) } // <-- forward the click
                        )
                    }
                }

                // Pagination buttons
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Button(
                        onClick = { if (currentPage > 0) currentPage-- },
                        enabled = currentPage > 0
                    ) { Text("Previous") }

                    Text(
                        text = "Page ${currentPage + 1} of $totalPages",
                        modifier = Modifier.align(Alignment.CenterVertically)
                    )

                    Button(
                        onClick = { if (currentPage < totalPages - 1) currentPage++ },
                        enabled = currentPage < totalPages - 1
                    ) { Text("Next") }
                }
            }
        }
    }
}