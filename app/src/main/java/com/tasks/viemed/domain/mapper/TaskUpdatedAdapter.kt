package com.tasks.viemed.domain.mapper

import com.tasks.viemed.domain.model.Task
import com.tasks.viewmed.CreateNewTaskMutation
import com.tasks.viewmed.UpdateTaskMutation

object TaskUpdatedAdapter : DomainEntityMapper<UpdateTaskMutation.UpdateTaskStatus, Task> {
    override fun toEntity(d: Task): UpdateTaskMutation.UpdateTaskStatus {
        return UpdateTaskMutation.UpdateTaskStatus(d.id, d.name, d.note, d.isDone)
    }

    override fun toDomain(e: UpdateTaskMutation.UpdateTaskStatus): Task {
        return Task(e.id, e.name, e.note, e.isDone)
    }

    @JvmName("taskUpdatedToDomain")
    fun UpdateTaskMutation.UpdateTaskStatus?.toDomain() : Task? {
        return this?.let { createTask -> toDomain(createTask) }
    }
}