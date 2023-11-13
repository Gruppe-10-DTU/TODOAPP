package com.gruppe11.todoApp.ui.elements

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun prioritySelection (
    priority: String,
    label: String,
    onPrioritySelected: (String) -> Unit
){
    FilterChip(
        selected = priority == label,
        onClick = { onPrioritySelected(label)},
        label = { Text(label.lowercase().replaceFirstChar { it.uppercase() })},
        enabled = true,
        colors = FilterChipDefaults.filterChipColors(
            containerColor = Color.Transparent,
            selectedContainerColor = MaterialTheme.colorScheme.primary,
            selectedLabelColor = MaterialTheme.colorScheme.background
        ),
        border = FilterChipDefaults.filterChipBorder(
            borderColor = MaterialTheme.colorScheme.primary,
            selectedBorderColor = Color.Transparent
        )
    )
}