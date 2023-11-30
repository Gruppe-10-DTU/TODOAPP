package com.gruppe11.todoApp.ui.elements

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.gruppe11.todoApp.R
import com.gruppe11.todoApp.viewModel.TaskViewModel

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalLayoutApi::class, ExperimentalMaterial3Api::class)
@Composable
fun FilterSection(
    taskViewModel: TaskViewModel
) {
    FlowRow {
        FilterChip(
            onClick = {
                taskViewModel.completeFilter.value = !taskViewModel.completeFilter.value
                uncheckOnAllSelected(taskViewModel)
            },
            label = {
                Text(stringResource(id = R.string.complete))
            },
            selected = taskViewModel.completeFilter.value,
            modifier = Modifier
                .padding(horizontal = 4.dp)
        )

        FilterChip(
            onClick = {
                taskViewModel.incompleteFilter.value = !taskViewModel.incompleteFilter.value
                uncheckOnAllSelected(taskViewModel)
            },
            label = {
                Text(stringResource(R.string.incomplete))
            },
            selected = taskViewModel.incompleteFilter.value,
            modifier = Modifier
                .padding(horizontal = 4.dp)
        )

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

@RequiresApi(Build.VERSION_CODES.O)
private fun uncheckOnAllSelected(taskViewModel: TaskViewModel) {
    if (taskViewModel.tags.all { it.checked }) {
        taskViewModel.tags.onEach { it.checked = false}
    }

    if (taskViewModel.completeFilter.value && taskViewModel.incompleteFilter.value) {
        taskViewModel.completeFilter.value = false
        taskViewModel.incompleteFilter.value = false
    }
}

