package com.tasks.viemed.data.repository

import com.apollographql.apollo3.api.ApolloResponse
import com.tasks.viemed.data.api.ApolloInstance
import com.tasks.viemed.domain.ViemedApiRepository
import com.tasks.viewmed.*
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ViemedApiRepositoryImpl @Inject constructor(private val apiService: ApolloInstance.Companion): ViemedApiRepository {

    override suspend fun fetchTasks(): Flow<ApolloResponse<AllTasksListQuery.Data>> {
        return apiService.BUILDER.query(AllTasksListQuery()).toFlow()
    }

    override suspend fun createTask(name: String, note: String): Flow<ApolloResponse<CreateNewTaskMutation.Data>> {
        return apiService.BUILDER.mutation(CreateNewTaskMutation(name, note)).toFlow()
    }

    override suspend fun updateTask(taskId: String, status: Boolean): Flow<ApolloResponse<UpdateTaskMutation.Data>> {
        return apiService.BUILDER.mutation(UpdateTaskMutation(taskId, status)).toFlow()
    }

    override suspend fun deleteTask(taskId: String): Flow<ApolloResponse<DeleteTaskMutation.Data>> {
        return apiService.BUILDER.mutation(DeleteTaskMutation(taskId)).toFlow()
    }

}