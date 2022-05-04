package com.tasks.viemed.domain.repository

import com.apollographql.apollo3.api.ApolloResponse
import com.tasks.viewmed.*
import kotlinx.coroutines.flow.Flow

interface ViemedApiRepository {

    suspend fun fetchTasks(): Flow<ApolloResponse<AllTasksListQuery.Data>>
    suspend fun createTask(name: String, note: String): Flow<ApolloResponse<CreateNewTaskMutation.Data>>
    suspend fun updateTask(taskId: String,status: Boolean): Flow<ApolloResponse<UpdateTaskMutation.Data>>
    suspend fun deleteTask(taskId: String): Flow<ApolloResponse<DeleteTaskMutation.Data>>

}