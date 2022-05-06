package com.tasks.viemed.domain.repository

import com.apollographql.apollo3.api.ApolloResponse
import com.tasks.viewmed.*
import kotlinx.coroutines.flow.Flow

interface ViemedApiRepository {

    suspend fun generateToken(): ApolloResponse<GenerateTokenMutation.Data>
    suspend fun fetchTasks(token: String): ApolloResponse<AllTasksListQuery.Data>
    suspend fun createTask(token: String, name: String, note: String): ApolloResponse<CreateNewTaskMutation.Data>
    suspend fun updateTask(token: String, taskId: String,status: Boolean): ApolloResponse<UpdateTaskMutation.Data>
    suspend fun deleteTask(token: String, taskId: String): ApolloResponse<DeleteTaskMutation.Data>

}