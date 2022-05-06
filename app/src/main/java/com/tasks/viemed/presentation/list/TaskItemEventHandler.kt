package com.tasks.viemed.presentation.list

import com.tasks.viemed.domain.model.Task

interface TaskItemEventHandler {

    fun onMoreActionsIconClicked(task: Task)
    fun onStatusDoneClicked(task: Task)

}