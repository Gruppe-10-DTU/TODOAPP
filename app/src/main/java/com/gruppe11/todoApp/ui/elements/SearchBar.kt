package com.gruppe11.todoApp.ui.elements

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.gruppe11.todoApp.ui.screenStates.TasksScreenState
import com.gruppe11.todoApp.viewModel.TaskViewModel

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun SearchBar(
    state: TasksScreenState
) {
    val viewModel = viewModel<TaskViewModel>()
    var searchText = state.searchText

    val trailingIconView = @Composable {
        IconButton(
            onClick = {
                viewModel.onSearchTextChange("")
            },
        ) {
            Icon(
                Icons.Default.Clear,
                contentDescription = "",
                tint = Color.Black
            )
        }
    }

    TextField(
        value = searchText,
        onValueChange = viewModel::onSearchTextChange,
        label = { Text("Search") },
        leadingIcon = { Icon(Icons.Filled.Search, contentDescription = null) },
        trailingIcon = if (searchText.isNotBlank()) trailingIconView else null,
        singleLine = true,
        modifier = Modifier
            .width(250.dp)
            .verticalScroll(rememberScrollState())
            .border(width = 1.5.dp, color = MaterialTheme.colorScheme.primary, RoundedCornerShape(12.dp)),
        colors = TextFieldDefaults.colors(
            focusedContainerColor = Color.Transparent,
            unfocusedContainerColor = Color.Transparent,
            disabledContainerColor = Color.Transparent,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            disabledIndicatorColor = Color.Transparent,
            disabledTextColor = Color.Transparent,
        )
    )
}



