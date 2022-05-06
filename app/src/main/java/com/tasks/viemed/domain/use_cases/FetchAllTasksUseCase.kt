package com.tasks.viemed.domain.use_cases

import com.apollographql.apollo3.api.ApolloResponse
import com.apollographql.apollo3.exception.ApolloException
import com.tasks.viemed.base.DataResource
import com.tasks.viemed.domain.repository.ViemedApiRepository
import com.tasks.viemed.domain.mapper.TaskAdapter
import com.tasks.viemed.domain.model.Task
import com.tasks.viewmed.AllTasksListQuery
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import okhttp3.internal.assertThreadHoldsLock
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class FetchAllTasksUseCase @Inject constructor(private val apiRepository: ViemedApiRepository) {

    operator fun invoke(token: String): Flow<DataResource<List<Task>>> = flow {
        try {
            emit(DataResource.Loading())
            val fetchTasksResponse = apiRepository.fetchTasks(token)
            when {
                fetchTasksResponse.data != null -> emit(
                    DataResource.Success(
                        taskList(
                            fetchTasksResponse
                        )
                    )
                )
                fetchTasksResponse.hasErrors() -> fetchTasksResponse.errors?.last()?.let {
                    emit(DataResource.Error(it.message))
                }
                else -> emit(DataResource.Error("An error occurred retrieving the task list"))
            }
        } catch (e: ApolloException) {
            emit(DataResource.Error(e.localizedMessage ?: "An unexpected error occurred"))
        } catch (e: HttpException) {
            emit(DataResource.Error(e.localizedMessage ?: "An unexpected error occurred"))
        } catch (e: IOException) {
            emit(DataResource.Error("Couldn't reach server. Check your internet connection."))
        }
    }

    private fun taskList(apolloResponse: ApolloResponse<AllTasksListQuery.Data>): List<Task> {
        return apolloResponse.data?.allTasks?.filterNotNull()
            ?.map { task -> TaskAdapter.toDomain(task) } ?: kotlin.run { emptyList() }
    }

}