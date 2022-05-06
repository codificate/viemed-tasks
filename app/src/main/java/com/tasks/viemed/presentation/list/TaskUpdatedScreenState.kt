package com.tasks.viemed.presentation.list

import com.tasks.viemed.domain.model.Task

data class TaskUpdatedScreenState(
    val isLoading: Boolean = false,
    val task: Task? = null,
    val error: String = ""
)
