package com.tasks.viemed.presentation.list

data class TaskDeletedScreenState(
    val isLoading: Boolean = false,
    val taskIdWasDeleted: String? = null,
    val error: String = ""
)
