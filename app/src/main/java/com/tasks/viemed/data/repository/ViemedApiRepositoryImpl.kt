package com.tasks.viemed.data.repository

import com.apollographql.apollo3.api.ApolloResponse
import com.apollographql.apollo3.network.okHttpClient
import com.tasks.viemed.BuildConfig
import com.tasks.viemed.data.api.ApolloInstance
import com.tasks.viemed.domain.repository.ViemedApiRepository
import com.tasks.viewmed.*
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ViemedApiRepositoryImpl @Inject constructor(private val apiService: ApolloInstance.Companion) :
    ViemedApiRepository {

    override suspend fun generateToken(): ApolloResponse<GenerateTokenMutation.Data> {
        return apiService.BUILDER.build().mutation(
            GenerateTokenMutation(
                BuildConfig.VIEMED_API_KEY,
                BuildConfig.VIEMED_USER_NAME
            )
        ).execute()
    }

    override suspend fun fetchTasks(token: String): ApolloResponse<AllTasksListQuery.Data> {
        return buildServiceWith(token).query(AllTasksListQuery()).execute()
    }

    override suspend fun createTask(
        token: String,
        name: String,
        note: String
    ): ApolloResponse<CreateNewTaskMutation.Data> {
        return buildServiceWith(token).mutation(CreateNewTaskMutation(name, note)).execute()
    }

    override suspend fun updateTask(
        token: String,
        taskId: String,
        status: Boolean
    ): ApolloResponse<UpdateTaskMutation.Data> {
        return buildServiceWith(token).mutation(UpdateTaskMutation(taskId, status)).execute()
    }

    override suspend fun deleteTask(token: String, taskId: String): ApolloResponse<DeleteTaskMutation.Data> {
        return buildServiceWith(token).mutation(DeleteTaskMutation(taskId)).execute()
    }

    private fun buildServiceWith(token: String) = apiService.BUILDER
        .okHttpClient(ApolloInstance.setTokenAuthorization(token))
        .build()

}