package com.gruppe11.todoApp.viewModel

import androidx.lifecycle.ViewModel
import com.gruppe11.todoApp.ui.elements.Tag

class FilterViewModel : ViewModel() {
    private val _filterTags = getFilterTags().toMutableList()

    val tags: List<Tag>
        get() = _filterTags

    private fun getFilterTags() = listOf(
        Tag(1, "Completed"),
        Tag(2, "Not completed")
    )
}