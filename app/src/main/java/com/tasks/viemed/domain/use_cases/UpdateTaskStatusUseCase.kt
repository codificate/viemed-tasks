package com.tasks.viemed.domain.use_cases

import com.apollographql.apollo3.exception.ApolloException
import com.tasks.viemed.base.DataResource
import com.tasks.viemed.domain.mapper.TaskUpdatedAdapter.toDomain
import com.tasks.viemed.domain.model.Task
import com.tasks.viemed.domain.repository.ViemedApiRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onEach
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class UpdateTaskStatusUseCase @Inject constructor(private val apiRepository: ViemedApiRepository) {

    operator fun invoke(token: String, taskId: String, status: Boolean): Flow<DataResource<Task>> =
        flow {
            try {
                emit(DataResource.Loading())
                val taskUpdatedResponse = apiRepository.updateTask(token, taskId, status)
                when {
                    taskUpdatedResponse.data != null -> taskUpdatedResponse.data?.updateTaskStatus.toDomain()
                        ?.let { emit(DataResource.Success(it)) } ?: kotlin.run {
                        emit(DataResource.Error("An unexpected error occurred updating the task status"))
                    }
                    taskUpdatedResponse.hasErrors() -> taskUpdatedResponse.errors?.last()?.let {
                        emit(DataResource.Error(it.message))
                    }
                }
            } catch (e: ApolloException) {
                emit(DataResource.Error(e.localizedMessage ?: "An unexpected error occurred"))
            } catch (e: HttpException) {
                emit(DataResource.Error(e.localizedMessage ?: "An unexpected error occurred"))
            } catch (e: IOException) {
                emit(DataResource.Error("Couldn't reach server. Check your internet connection."))
            }
        }

}