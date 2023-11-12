package com.gruppe11.todoApp.viewModel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.gruppe11.todoApp.model.Tag

class FilterViewModel : ViewModel() {
    private val _filterTags = getFilterTags().toMutableSet()
    var complete = mutableStateOf(false)
    var incomplete = mutableStateOf(false)

    val tags: Set<Tag>
        get() = _filterTags

    private fun getFilterTags() = emptySet<Tag>()
}