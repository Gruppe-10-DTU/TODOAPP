package com.gruppe11.todoApp.ui.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.gruppe11.todoApp.ui.theme.TODOAPPTheme

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter", "NewApi")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilterScreen() {

    // Temporary vars
    var completionSelected by remember { mutableStateOf(false) }
    var incompletionSelected by remember { mutableStateOf(false) }

    Scaffold(
        modifier = Modifier.fillMaxHeight(),
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        "Filter",
                        maxLines = 1,
                    )
                },
                navigationIcon = {
                    IconButton(onClick = {}) {
                        Icon(
                            imageVector = Icons.Filled.Close,
                            contentDescription = "Close filter screen",
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }
            )
        }
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(10.dp)
        ) {
            // Replace the following code with actually generated tags
            // Completion chip
            FilterChip(
                onClick = { completionSelected = !completionSelected },
                label = {
                    Text("Completed")
                },
                selected = completionSelected,
                leadingIcon = if (completionSelected) {
                    {
                        Icon(
                            imageVector = Icons.Filled.Done,
                            contentDescription = "Done icon",
                            modifier = Modifier.size(FilterChipDefaults.IconSize)
                        )
                    }
                } else {
                    null
                },
            )

            // Incompletion chip
            FilterChip(
                onClick = { incompletionSelected = !incompletionSelected },
                label = {
                    Text("Not completed")
                },
                selected = incompletionSelected,
                leadingIcon = if (incompletionSelected) {
                    {
                        Icon(
                            imageVector = Icons.Filled.Done,
                            contentDescription = "Done icon",
                            modifier = Modifier.size(FilterChipDefaults.IconSize)
                        )
                    }
                } else {
                    null
                },
            )

        }
    }
}

@Preview
@Composable
fun FilterScreenPreview() {
    TODOAPPTheme() {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ){
            FilterScreen()
        }
    }
}