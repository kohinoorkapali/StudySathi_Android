package com.example.studysathi.view

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.studysathi.model.MaterialModel
import com.example.studysathi.utlis.SessionManager


@Composable
fun MaterialCard(
    material: MaterialModel,
    showActions: Boolean = false,
    onEdit: (() -> Unit)? = null,
    onDelete: (() -> Unit)? = null
) {
    val currentUser = SessionManager.currentUser

    Card(
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFE1F5FE)),
        border = BorderStroke(1.dp, Color(0xFFBBDEFB)),
        elevation = CardDefaults.cardElevation(6.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = material.title, style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = material.description, style = MaterialTheme.typography.bodyMedium)
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Uploaded by: ${material.uploadedBy}",
                style = MaterialTheme.typography.bodySmall.copy(color = Color.DarkGray)
            )
            Text(
                text = "Stream: ${material.stream}",
                style = MaterialTheme.typography.bodySmall.copy(color = Color.DarkGray)
            )

            if (showActions && material.uploadedById == currentUser?.id) {
                Spacer(modifier = Modifier.height(12.dp))
                Row(
                    horizontalArrangement = Arrangement.End,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    IconButton(onClick = { onEdit?.invoke() }) {
                        Icon(
                            painter = painterResource(id = com.example.studysathi.R.drawable.baseline_edit_24),
                            contentDescription = "Edit Material",
                            tint = Color(0xFF0D47A1)
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    IconButton(onClick = { onDelete?.invoke() }) {
                        Icon(
                            painter = painterResource(id = com.example.studysathi.R.drawable.baseline_delete_24),
                            contentDescription = "Delete Material",
                            tint = Color.Red
                        )
                    }
                }
            }
        }
    }
}