package com.example.studysathi.view

import android.app.Activity
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
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
import com.example.studysathi.repository.CommonRepoImpl
import com.example.studysathi.repository.MaterialRepoImpl
import com.example.studysathi.ui.theme.Gray500
import com.example.studysathi.ui.theme.SoftBlue
import com.example.studysathi.ui.theme.UploadColor
import com.example.studysathi.ui.theme.White
import com.example.studysathi.utlis.SessionManager
import com.example.studysathi.view.ui.theme.StudySathiTheme
import com.example.studysathi.viewmodel.CommonViewModel
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

    val context = LocalContext.current
    val backDispatcher =
        LocalOnBackPressedDispatcherOwner.current?.onBackPressedDispatcher

    val materialViewModel = remember { MaterialViewModel(MaterialRepoImpl()) }
    val commonViewModel = remember { CommonViewModel(CommonRepoImpl()) }

    val title by materialViewModel.title.collectAsState()
    val stream by materialViewModel.stream.collectAsState()
    val description by materialViewModel.description.collectAsState()
    val status by materialViewModel.status.collectAsState()
    val fileName by materialViewModel.fileName.collectAsState()

    var expanded by remember { mutableStateOf(false) }

    val streams = listOf(
        "Science",
        "Management",
        "Humanities",
        "Engineering",
        "Medical",
        "Law",
        "Other"
    )

    // File picker
    val fileLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            uri?.let {
                val name = commonViewModel.getFileName(context, it) ?: "file"
                materialViewModel.setFile(it, name)
                Toast.makeText(context, "Selected: $name", Toast.LENGTH_SHORT).show()
            }
        }



    // Status toast
    LaunchedEffect(status) {
        status?.let {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
            materialViewModel.clearStatus()
            if (it.contains("success", true)) {
                backDispatcher?.onBackPressed()
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Upload Resource", color = White) },
                navigationIcon = {
                    Icon(
                        painter = painterResource(id = R.drawable.baseline_arrow_back_ios_24),
                        contentDescription = "Back",
                        tint = White,
                        modifier = Modifier
                            .clickable { backDispatcher?.onBackPressed() }
                            .padding(12.dp)
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = SoftBlue
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
                    .padding(16.dp)
            ) {

                LazyColumn(verticalArrangement = Arrangement.spacedBy(16.dp)) {

                    // Title
                    item {
                        Text("Resource Title")
                        OutlinedTextField(
                            value = title,
                            onValueChange = materialViewModel::setTitle,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }

                    // Stream
                    item {
                        Text("Stream")
                        ExposedDropdownMenuBox(
                            expanded = expanded,
                            onExpandedChange = { expanded = !expanded }
                        ) {
                            OutlinedTextField(
                                value = stream,
                                onValueChange = {},
                                readOnly = true,
                                modifier = Modifier.menuAnchor().fillMaxWidth(),
                                trailingIcon = {
                                    ExposedDropdownMenuDefaults.TrailingIcon(expanded)
                                }
                            )
                            ExposedDropdownMenu(
                                expanded = expanded,
                                onDismissRequest = { expanded = false }
                            ) {
                                streams.forEach {
                                    DropdownMenuItem(
                                        text = { Text(it) },
                                        onClick = {
                                            materialViewModel.setStream(it)
                                            expanded = false
                                        }
                                    )
                                }
                            }
                        }
                    }

                    // Description
                    item {
                        Text("Description")
                        OutlinedTextField(
                            value = description,
                            onValueChange = materialViewModel::setDescription,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(120.dp)
                        )
                    }

                    // Upload box
                    item {
                        Text("Upload File")
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(120.dp)
                                .border(
                                    BorderStroke(1.dp, UploadColor),
                                    RoundedCornerShape(8.dp)
                                )
                                .clickable { fileLauncher.launch("*/*") },
                            contentAlignment = Alignment.Center
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Icon(
                                    painter = painterResource(R.drawable.outline_upload_24),
                                    contentDescription = null,
                                    tint = Gray500
                                )
                                Spacer(Modifier.height(8.dp))
                                Text(
                                    text = if (fileName.isNotEmpty())
                                        fileName
                                    else "Upload PDF, PPT, Word",
                                    color = Gray500
                                )
                            }
                        }
                    }

                    // Upload button
                    item {
                        Button(
                            onClick = {
                                val currentUser = SessionManager.currentUser
                                if (currentUser == null) {
                                    Toast.makeText(context, "User not logged in", Toast.LENGTH_SHORT).show()
                                    return@Button
                                }

                                val fileUri = materialViewModel.fileUri.value
                                if (fileUri == null) {
                                    Toast.makeText(context, "Please select a file", Toast.LENGTH_SHORT).show()
                                    return@Button
                                }

                                // Upload file to Cloudinary
                                commonViewModel.uploadFile(context, fileUri) { url ->
                                    if (url != null) {
                                        // Pass the logged-in user info when saving
                                        materialViewModel.uploadMaterial(
                                            uploadedBy = currentUser.fullName, // or username
                                            fileUrl = url,

                                        )
                                    } else {
                                        Toast.makeText(context, "Upload failed", Toast.LENGTH_SHORT).show()
                                    }
                                }
                            },
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = SoftBlue,
                                contentColor = White
                            )
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