package com.tasks.viemed.presentation.list

import com.tasks.viemed.domain.model.Task

data class ListTaskScreenState(
    val isLoading: Boolean = false,
    val tasks: List<Task> = emptyList(),
    val error: String = ""
)
