package com.tasks.viemed.presentation.create

import com.tasks.viemed.domain.model.Task

data class CreateTasScreenState(
    val isLoading: Boolean = false,
    val task: Task? = null,
    val error: String = ""
)
