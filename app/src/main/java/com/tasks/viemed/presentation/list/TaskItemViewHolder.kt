package com.tasks.viemed.presentation.list

import androidx.recyclerview.widget.RecyclerView
import com.tasks.viemed.databinding.TaskRowBinding
import com.tasks.viemed.domain.model.Task

class TaskItemViewHolder(
    private val binding: TaskRowBinding,
    private val listener: TaskItemEventHandler
) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(task: Task) {
        binding.task = task
        binding.eventHandler = listener
    }

}