package com.example.studysathi

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.studysathi.ui.theme.StudySathiTheme
import com.example.studysathi.view.AddMaterial
import com.example.studysathi.view.HomeScreen
import com.example.studysathi.view.NotificationScreen
import com.example.studysathi.view.ProfileScreen
import com.example.studysathi.view.SearchScreen

class DashboardActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            DashboardBody()
        }
    }
}

@Composable
fun DashboardBody() {
    val context = LocalContext.current
    var selectedIndex by remember { mutableStateOf(0) }

    data class NavItem(val icon: Int)

    val listItems = listOf(
        NavItem(R.drawable.baseline_home_24),
        NavItem( R.drawable.baseline_search_24),
        NavItem(R.drawable.baseline_notifications_24),
        NavItem( R.drawable.baseline_person_24),
    )

    Box(modifier = Modifier.fillMaxSize()) {

        Scaffold(
            bottomBar = {
                NavigationBar(
                    containerColor = MaterialTheme.colorScheme.surface,
                    tonalElevation = 8.dp
                ) {
                    listItems.forEachIndexed { index, item ->

                        // Add empty slot in the middle for FAB
                        if (index == 2) {
                            NavigationBarItem(
                                icon = {},
                                label = {},
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
                                    painter = painterResource(id = item.icon),
                                    contentDescription = "Icon $index",
                                    modifier = Modifier.size(32.dp)
                                )
                            },

                        )
                    }
                }
            }
        ) { paddingValues ->

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                when (selectedIndex) {
                    0 -> HomeScreen()
                    1 -> SearchScreen()
                    2 -> NotificationScreen()
                    3 -> ProfileScreen()
                }
            }
        }

        // CENTER FAB
        FloatingActionButton(
            onClick = {
                context.startActivity(Intent(context, AddMaterial::class.java))
            },
            shape = CircleShape,
            containerColor = MaterialTheme.colorScheme.primary, // Blue fill
            contentColor = MaterialTheme.colorScheme.onPrimary, // White plus icon
            modifier = Modifier
                .align(Alignment.BottomCenter) // Bottom center of screen
                .padding(bottom = 32.dp) // overlap navbar slightly
                .size(64.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = "Add",
                modifier = Modifier.size(32.dp)
            )
        }
    }
}
