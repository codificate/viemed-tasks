package com.tasks.viemed.presentation.list

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tasks.viemed.databinding.TaskRowBinding
import com.tasks.viemed.domain.model.Task

class TaskListAdapter(private val listener: TaskItemEventHandler) : RecyclerView.Adapter<TaskItemViewHolder>(){

    private val taskList = mutableListOf<Task>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskItemViewHolder {
        val binding = TaskRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TaskItemViewHolder(binding, listener)
    }

    override fun onBindViewHolder(holder: TaskItemViewHolder, position: Int) {
        holder.bind(taskList[position])
    }

    override fun getItemCount(): Int = taskList.size

    @SuppressLint("NotifyDataSetChanged")
    fun setTasksList(tasks: List<Task>) {
        taskList.removeAll(taskList)
        taskList.sortedBy { task -> task.isDone }.let {
            taskList.addAll(tasks)
        }
        notifyDataSetChanged()
    }

    fun updateTask(taskUpdated: Task) {
        var taskUpdatedPosition = 0
        taskList.forEachIndexed { index, task ->
            if (task.id == taskUpdated.id) {
                taskList[index] = taskUpdated
                taskUpdatedPosition = index
            }
        }
        notifyItemChanged(taskUpdatedPosition)
    }

    fun removeTask(taskIdWasDeleted: String) {
        var taskRemovedPosition = 0
        taskList.forEachIndexed { index, task ->
            if (task.id == taskIdWasDeleted) {
                taskList.removeAt(index)
                taskRemovedPosition = index
            }
        }
        notifyItemRemoved(taskRemovedPosition)
    }
}