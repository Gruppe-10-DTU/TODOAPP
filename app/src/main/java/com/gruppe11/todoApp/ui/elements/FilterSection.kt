package com.gruppe11.todoApp.ui.elements

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
import androidx.lifecycle.viewmodel.compose.viewModel
import com.gruppe11.todoApp.R
import com.gruppe11.todoApp.viewModel.FilterViewModel

@OptIn(ExperimentalLayoutApi::class, ExperimentalMaterial3Api::class)
@Composable
fun FilterSection(
    filterViewModel: FilterViewModel = viewModel()
) {
    FlowRow {
        FilterChip(
            onClick = {
                filterViewModel.complete.value = !filterViewModel.complete.value
                uncheckOnAllSelected(filterViewModel)
            },
            label = {
                Text(stringResource(id = R.string.complete))
            },
            selected = filterViewModel.complete.value,
            modifier = Modifier
                .padding(horizontal = 4.dp)
        )

        FilterChip(
            onClick = {
                filterViewModel.incomplete.value = !filterViewModel.incomplete.value
                uncheckOnAllSelected(filterViewModel)
            },
            label = {
                Text(stringResource(R.string.incomplete))
            },
            selected = filterViewModel.incomplete.value,
            modifier = Modifier
                .padding(horizontal = 4.dp)
        )

        filterViewModel.tags.forEach { tag ->
            FilterChip(
                onClick = {
                    tag.checked = !tag.checked
                    uncheckOnAllSelected(filterViewModel)
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

private fun uncheckOnAllSelected(filterViewModel: FilterViewModel) {
    if (filterViewModel.tags.all { it.checked }) {
        filterViewModel.tags.onEach { it.checked = false}
    }

    if (filterViewModel.complete.value && filterViewModel.incomplete.value) {
        filterViewModel.complete.value = false
        filterViewModel.incomplete.value = false
    }
}

