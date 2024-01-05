package com.gruppe11.todoApp.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.RemoveCircleOutline
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.gruppe11.todoApp.model.TimeSlot
import com.gruppe11.todoApp.ui.elements.SwitchableButton
import com.gruppe11.todoApp.viewModel.ScheduleViewModel
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ManageTimeSlots(
    viewModel: ScheduleViewModel,
    returnPage: () -> Unit
) {
    val timeSlots = viewModel.timeSlots.collectAsStateWithLifecycle(initialValue = emptyList())
    var tmpList = emptyList<TimeSlot>()
    timeSlots.value.forEach{ tmpList = tmpList.plus(it.copy())}
    Scaffold(
        modifier = Modifier.fillMaxHeight(),
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Manage time slots", maxLines = 1
                    )
                },
                navigationIcon = {
                    IconButton(onClick = returnPage) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Go back",
                            modifier = Modifier.size(24.dp)
                        )
                    }
                },
            )
        },
        bottomBar = {
            HorizontalDivider()
            BottomAppBar(
                containerColor = MaterialTheme.colorScheme.background,
            ) {
                //Cancel button
                SwitchableButton(
                    text = "Cancel",
                    onClick = { returnPage() },
                    isFilled = false,
                    pickedColor = MaterialTheme.colorScheme.primary,
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp)
                )

                // Save button
                SwitchableButton(
                    text = "Save",
                    onClick = { /*TODO*/ },
                    isFilled = true,
                    pickedColor = MaterialTheme.colorScheme.tertiary,
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp)
                )
            }
        }
    ) {padding ->
        LazyColumn(
            modifier = Modifier.padding(padding)
        ) {
            items(tmpList.sortedBy { it.start }){
                EditableTimeSlot(
                    timeSlot = it,
                    onDelete = { tmpList = tmpList.filterNot { element -> element.id == it.id } },
                )
                HorizontalDivider(thickness = 1.dp)
            }

        }

    }
}

@Composable
fun EditableTimeSlot(
    timeSlot: TimeSlot,
    onDelete: () -> Unit,
){
    Row(modifier = Modifier.padding(15.dp,5.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = onDelete) {
            Icon(
                imageVector = Icons.Default.RemoveCircleOutline,
                contentDescription = null,
                modifier = Modifier.scale(1.5F))
        }
        Column(modifier = Modifier.padding(5.dp,0.dp)) {
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = timeSlot.name,
                onValueChange = { /*TODO*/ },
                label = { Text(text = "Name")}
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(0.dp, 5.dp),
                horizontalArrangement = Arrangement.SpaceBetween) {
                OutlinedTextField(
                    modifier = Modifier.width(150.dp),
                    value = timeSlot.start.format(DateTimeFormatter.ofPattern("HH:mm")),
                    onValueChange = { /*TODO*/ },
                    label = { Text(text = "Start")}
                )
                OutlinedTextField(
                    modifier = Modifier.width(150.dp),
                    value = timeSlot.end.format(DateTimeFormatter.ofPattern("HH:mm")),
                    onValueChange = { /*TODO*/ },
                    label = { Text(text = "End")}
                )

            }
        }
    }


}