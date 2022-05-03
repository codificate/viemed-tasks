package com.tasks.viemed.domain.mapper

import com.tasks.viemed.domain.model.Task
import com.tasks.viewmed.AllTasksListQuery

object TaskAdapter : DomainEntityMapper<AllTasksListQuery.AllTask, Task>{

    override fun toEntity(d: Task): AllTasksListQuery.AllTask {
        return AllTasksListQuery.AllTask(
            id = d.id,
            name = d.name,
            note = d.note,
            isDone = d.isDone,
        )
    }

    override fun toDomain(e: AllTasksListQuery.AllTask): Task {
        return Task(
            e.id,
            e.name,
            e.note,
            e.isDone,
        )
    }

}