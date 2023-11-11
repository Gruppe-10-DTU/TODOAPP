package com.gruppe11.todoApp.ui.elements

import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.gruppe11.todoApp.viewModel.FilterViewModel

@OptIn(ExperimentalLayoutApi::class, ExperimentalMaterial3Api::class)
@Composable
fun FilterTags(
    filterViewModel: FilterViewModel = viewModel()
) {
    FlowRow {
        filterViewModel.tags.forEach { tag ->
            FilterChip(
                onClick = {
                    tag.checked = !tag.checked

                    // If all tags are selected, unselect all automatically
                    if (filterViewModel.tags.all { it.checked }) {
                        filterViewModel.tags.onEach { it.checked = false}
                    }
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