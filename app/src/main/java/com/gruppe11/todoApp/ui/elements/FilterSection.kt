package com.gruppe11.todoApp.ui.elements

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.capitalize
import androidx.compose.ui.unit.dp
import com.gruppe11.todoApp.R
import com.gruppe11.todoApp.model.Priority
import com.gruppe11.todoApp.ui.screenStates.TasksScreenState
import com.gruppe11.todoApp.viewModel.TaskViewModel

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalLayoutApi::class, ExperimentalMaterial3Api::class)
@Composable
fun FilterSection(
    taskViewModel: TaskViewModel,
    state: TasksScreenState
) {
    Column() {
        FlowRow {
            FilterChip(
                onClick = {
                    taskViewModel.changeFilter("complete")
                    uncheckOnAllSelected(taskViewModel)
                },
                label = {
                    Text(stringResource(id = R.string.complete))
                },
                selected = state.completeFilter,
                modifier = Modifier
                    .padding(horizontal = 4.dp)
            )

            FilterChip(
                onClick = {
                    taskViewModel.changeFilter("incomplete")
                    uncheckOnAllSelected(taskViewModel)
                },
                label = {
                    Text(stringResource(R.string.incomplete))
                },
                selected = state.incompleteFilter,
                modifier = Modifier
                    .padding(horizontal = 4.dp)
            )
        }
        FlowRow {
            Priority.values().forEach { priority ->
                FilterChip(
                    onClick = {
                        taskViewModel.addPriority(priority)
                    },
                    label = {
                        Text(priority.name.lowercase().replaceFirstChar { x -> x.uppercaseChar()})
                    },
                    selected = state.priorities.contains(priority),
                    modifier = Modifier
                        .padding(horizontal = 4.dp)
                )
            }
        }
        FlowRow {

            taskViewModel.tags.forEach { tag ->
                FilterChip(
                    onClick = {
                        tag.checked = !tag.checked
                        uncheckOnAllSelected(taskViewModel)
                    },
                    label = {
                        Text(tag.label)
                    },
                    selected = tag.checked,
                    modifier = Modifier
                        .padding(horizontal = 4.dp)
                )
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
private fun uncheckOnAllSelected(taskViewModel: TaskViewModel) {
    if (taskViewModel.tags.all { it.checked }) {
        taskViewModel.tags.onEach { it.checked = false}
    }
}

