package com.gruppe11.todoApp.ui.elements

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PriorityFC(
    selectedPriority: String,
    onClick: (String) -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier.fillMaxWidth()
    ) {
        Spacer(modifier = Modifier.width(20.dp))
        FilterChip(
            selected = selectedPriority == "LOW",
            onClick = { onClick("LOW") },
            label = { Text("Low") },
            enabled = true
        )
        FilterChip(
            selected = selectedPriority == "MEDIUM",
            onClick = { onClick("MEDIUM") },
            label = { Text("Medium") },
            enabled = true
        )
        FilterChip(
            selected = selectedPriority == "HIGH",
            onClick = { onClick("HIGH") },
            label = { Text("High") },
            enabled = true
        )
        Spacer(modifier = Modifier.width(20.dp))
    }
}