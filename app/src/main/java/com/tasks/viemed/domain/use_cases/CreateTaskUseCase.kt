package com.tasks.viemed.domain.use_cases

import com.apollographql.apollo3.exception.ApolloException
import com.tasks.viemed.base.DataResource
import com.tasks.viemed.domain.mapper.TaskAdapter
import com.tasks.viemed.domain.mapper.TaskCreatedAdapter
import com.tasks.viemed.domain.mapper.TaskCreatedAdapter.toDomain
import com.tasks.viemed.domain.model.Task
import com.tasks.viemed.domain.repository.ViemedApiRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onEach
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class CreateTaskUseCase @Inject constructor(private val apiRepository: ViemedApiRepository) {

    operator fun invoke(token: String, name: String, note: String): Flow<DataResource<Task>> =
        flow {
            try {
                emit(DataResource.Loading())
                val taskCreatedResponse = apiRepository.createTask(token, name, note)
                when {
                    taskCreatedResponse.data != null -> taskCreatedResponse.data?.createTask.toDomain()
                        ?.let { emit(DataResource.Success(it)) } ?: kotlin.run {
                        emit(DataResource.Error("An unexpected error occurred creating the task"))
                    }
                    taskCreatedResponse.hasErrors() -> taskCreatedResponse.errors?.last()?.let {
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