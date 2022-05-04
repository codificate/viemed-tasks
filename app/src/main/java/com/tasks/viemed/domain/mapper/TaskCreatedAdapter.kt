package com.tasks.viemed.domain.mapper

import com.tasks.viemed.domain.model.Task
import com.tasks.viewmed.CreateNewTaskMutation

object TaskCreatedAdapter : DomainEntityMapper<CreateNewTaskMutation.CreateTask, Task> {
    override fun toEntity(d: Task): CreateNewTaskMutation.CreateTask {
        return CreateNewTaskMutation.CreateTask(d.id, d.name, d.note, d.isDone)
    }

    override fun toDomain(e: CreateNewTaskMutation.CreateTask): Task {
        return Task(e.id, e.name, e.note, e.isDone)
    }

    @JvmName("taskCreatedToDomain")
    fun CreateNewTaskMutation.CreateTask?.toDomain() : Task? {
        return this?.let { createTask -> toDomain(createTask) }
    }
}