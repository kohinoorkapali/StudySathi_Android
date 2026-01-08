package com.example.studysathi.view

import android.app.Activity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.studysathi.R
import com.example.studysathi.repository.MaterialRepoImpl
import com.example.studysathi.ui.theme.Gray500
import com.example.studysathi.ui.theme.SoftBlue
import com.example.studysathi.ui.theme.UploadColor
import com.example.studysathi.ui.theme.White
import com.example.studysathi.view.ui.theme.StudySathiTheme
import com.example.studysathi.viewmodel.MaterialViewModel

class AddMaterial : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AddMaterialBody()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddMaterialBody() {
    val repo = remember { MaterialRepoImpl() }
    val viewModel = remember { MaterialViewModel(repo) }

    val context = LocalContext.current
    val activity = context as Activity

    // Collect states from ViewModel
    val title by viewModel.title.collectAsState()
    val stream by viewModel.stream.collectAsState()
    val description by viewModel.description.collectAsState()
    val status by viewModel.status.collectAsState()

    var expanded by remember { mutableStateOf(false) }
    val streams = listOf("Science", "Management", "Humanities", "Engineering", "Medical", "Law", "Other")

    // Show Toast when status changes
    LaunchedEffect (status) {
        status?.let {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
            viewModel.clearStatus()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Upload Notes", color = White) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = SoftBlue,
                    titleContentColor = White
                )
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(SoftBlue)
                .padding(paddingValues)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        color = White,
                        shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
                    )
                    .padding(horizontal = 16.dp, vertical = 24.dp)
            ) {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    // Title
                    item {
                        Text("Resource Title", style = MaterialTheme.typography.labelLarge)
                        OutlinedTextField(
                            value = title,
                            onValueChange = { viewModel.setTitle(it) },
                            modifier = Modifier.fillMaxWidth()
                        )
                    }

                    // Stream Dropdown
                    item {
                        Text("Select Stream", style = MaterialTheme.typography.labelLarge)
                        ExposedDropdownMenuBox(
                            expanded = expanded,
                            onExpandedChange = { expanded = !expanded }
                        ) {
                            OutlinedTextField(
                                value = stream,
                                onValueChange = {},
                                readOnly = true,
                                placeholder = { Text("Choose a Stream") },
                                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                                modifier = Modifier
                                    .menuAnchor()
                                    .fillMaxWidth()
                            )
                            ExposedDropdownMenu(
                                expanded = expanded,
                                onDismissRequest = { expanded = false }
                            ) {
                                streams.forEach { selectedStream ->
                                    DropdownMenuItem(
                                        text = { Text(selectedStream) },
                                        onClick = {
                                            viewModel.setStream(selectedStream)
                                            expanded = false
                                        }
                                    )
                                }
                            }
                        }
                    }

                    // Description
                    item {
                        Text("Description", style = MaterialTheme.typography.labelLarge)
                        OutlinedTextField(
                            value = description,
                            onValueChange = { viewModel.setDescription(it) },
                            placeholder = { Text("Describe the content, topics covered etc") },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(120.dp)
                        )
                    }

                    // Upload Box (just placeholder)
                    item {
                        Text("Upload", style = MaterialTheme.typography.labelLarge)
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(120.dp)
                                .border(BorderStroke(1.dp, UploadColor), shape = RoundedCornerShape(8.dp))
                                .clickable { /* TODO: file picker */ },
                            contentAlignment = Alignment.Center
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Icon(
                                    painter = painterResource(id = R.drawable.outline_upload_24),
                                    contentDescription = "Upload Icon",
                                    tint = Gray500
                                )
                                Spacer(Modifier.height(8.dp))
                                Text("Upload Pdf, ppt or word", color = Gray500)
                            }
                        }
                    }

                    // Submit Button
                    item {
                        Button(
                            onClick = { viewModel.uploadMaterial() },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = SoftBlue,
                                contentColor = White
                            ),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 16.dp)
                        ) {
                            Text("Upload")
                        }
                    }
                }
            }
        }
    }
}



@Preview(showBackground = true)
@Composable
fun AddMaterialPreview() {
    AddMaterialBody()
}