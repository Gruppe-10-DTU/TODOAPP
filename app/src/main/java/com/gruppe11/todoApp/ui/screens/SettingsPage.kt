package com.gruppe11.todoApp.ui.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.gruppe11.todoApp.model.TimeSlot

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter", "NewApi")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsPage(
    manageTimeSlot: () -> Unit
) {
    Scaffold(
        modifier = Modifier.fillMaxHeight(),
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Settings",
                        maxLines = 1
                    )
                },
            )
        },
    ) {padding ->
        LazyColumn(
            modifier = Modifier.padding(padding),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            item{
                TimeSlotSettings(navigation = manageTimeSlot)
            }
            item {
                ProfileSlotSettings(navigation = manageTimeSlot)
            }
        }

    }
}
@Composable
fun TimeSlotSettings(
    navigation: () -> Unit
){
    TextButton(
        onClick = navigation,
        colors = ButtonColors(
            containerColor = Color.Transparent,
            contentColor = MaterialTheme.colorScheme.onBackground,
            disabledContentColor = MaterialTheme.colorScheme.tertiary,
            disabledContainerColor = Color.Transparent
            ),
        modifier = Modifier.fillMaxWidth()
        ) {Alignment.CenterVertically
        Arrangement.SpaceBetween
        Icon(imageVector = Icons.Default.Schedule, contentDescription = null)
        Spacer(modifier = Modifier.width(25.dp))
        Text(text = "Manage time slots", fontSize = 25.sp)
        Spacer(modifier = Modifier.width(25.dp))
        Icon(
            imageVector = Icons.Default.ArrowBackIosNew,
            contentDescription = null,
            modifier = Modifier.rotate(180F)
        )
    }
}

@Composable
fun ProfileSlotSettings(
    navigation: () -> Unit
) {
    TextButton(
        onClick = navigation,
        colors = ButtonColors(
            containerColor = Color.Transparent,
            contentColor = MaterialTheme.colorScheme.onBackground,
            disabledContentColor = MaterialTheme.colorScheme.tertiary,
            disabledContainerColor = Color.Transparent
        ),
        modifier = Modifier.fillMaxWidth()
    ) {Alignment.CenterVertically
        Arrangement.SpaceBetween
        Icon(imageVector = Icons.Default.AccountCircle, contentDescription = null)
        Spacer(modifier = Modifier.width(25.dp))
        Text(text = "Manage Profile", fontSize = 25.sp)
        Spacer(modifier = Modifier.width(25.dp))
        Icon(
            imageVector = Icons.Default.ArrowBackIosNew,
            contentDescription = null,
            modifier = Modifier.rotate(180F)
        )
        
    }
}