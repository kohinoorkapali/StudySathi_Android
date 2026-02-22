package com.example.studysathi

import android.content.Intent
import android.os.Bundle
import android.se.omapi.Session
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.studysathi.view.AddMaterial
import com.example.studysathi.view.HomeScreen
import com.example.studysathi.view.ProfileScreen
import com.example.studysathi.view.SearchScreen
import com.example.studysathi.model.MaterialModel
import com.example.studysathi.model.Usermodel
import com.example.studysathi.utlis.SessionManager
import com.example.studysathi.view.MaterialDetailScreen
import com.example.studysathi.view.MyResource

class DashboardActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val currentUser = SessionManager.currentUser
        setContent {
            DashboardBody(currentUser)
        }
    }
}

@Composable
fun DashboardBody(currentUser: Usermodel?) {
    val context = LocalContext.current
    var selectedIndex by remember { mutableStateOf(0) }
    var selectedMaterial by remember { mutableStateOf<MaterialModel?>(null) }
    var previousIndex by remember { mutableStateOf(0) }

    fun openMaterial(material: MaterialModel) {
        previousIndex = selectedIndex
        selectedMaterial = material
        selectedIndex = 4 // Detail View
    }

    // We keep 4 items, but we will insert the "Gap" logic carefully
    val navItems = listOf(
        R.drawable.baseline_home_24,
        R.drawable.baseline_search_24,
        R.drawable.baseline_folder_open_24,
        R.drawable.baseline_person_24,
    )

    Box(modifier = Modifier.fillMaxSize()) {
        Scaffold(
            bottomBar = {
                NavigationBar(
                    containerColor = MaterialTheme.colorScheme.surface,
                    tonalElevation = 8.dp
                ) {
                    navItems.forEachIndexed { index, iconRes ->
                        // Add a placeholder spacer in the dead center
                        // Since we have 4 items, index 2 is the start of the second half
                        if (index == 2) {
                            NavigationBarItem(
                                icon = {},
                                selected = false,
                                onClick = {},
                                enabled = false
                            )
                        }

                        NavigationBarItem(
                            selected = selectedIndex == index,
                            onClick = { selectedIndex = index },
                            icon = {
                                Icon(
                                    painter = painterResource(id = iconRes),
                                    contentDescription = null,
                                    modifier = Modifier.size(26.dp) // Standard nav size
                                )
                            }
                        )
                    }
                }
            }
        ) { paddingValues ->
            Box(modifier = Modifier.padding(paddingValues)) {
                when (selectedIndex) {
                    0 -> HomeScreen(onMaterialClick = { openMaterial(it) })
                    1 -> SearchScreen(onMaterialClick = { openMaterial(it) })
                    2 -> MyResource(onMaterialClick = { openMaterial(it) })
                    3 -> ProfileScreen()
                    4 -> selectedMaterial?.let {
                        MaterialDetailScreen(
                            material = it,
                            onBack = { selectedIndex = previousIndex }
                        )
                    }
                }
            }
        }

        // CENTER FAB
        FloatingActionButton(
            onClick = {
                context.startActivity(Intent(context, AddMaterial::class.java))
            },
            shape = CircleShape,
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 44.dp) // Adjusted to sit better on the bar
                .size(64.dp)
        ) {
            Icon(
                // You mentioned using vector assets,
                // ensure R.drawable.ic_add exists or use Icons.Default.Add
                imageVector = Icons.Default.Add,
                contentDescription = "Add",
                modifier = Modifier.size(32.dp)
            )
        }
    }
}


