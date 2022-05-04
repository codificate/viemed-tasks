package com.tasks.viemed.domain.use_cases

import com.apollographql.apollo3.api.ApolloResponse
import com.apollographql.apollo3.exception.ApolloException
import com.tasks.viemed.base.DataResource
import com.tasks.viemed.domain.repository.ViemedApiRepository
import com.tasks.viemed.domain.mapper.TaskAdapter
import com.tasks.viemed.domain.model.Task
import com.tasks.viewmed.AllTasksListQuery
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onEach
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class FetchAllTasksUseCase @Inject constructor(private val apiRepository: ViemedApiRepository) {

    operator fun invoke(): Flow<DataResource<List<Task>>> = flow {
        try {
            emit(DataResource.Loading())
            apiRepository.fetchTasks().onEach { apolloResponse ->
                when {
                    apolloResponse.data != null -> emit(DataResource.Success(taskList(apolloResponse)))
                    apolloResponse.hasErrors() ->  apolloResponse.errors?.last()?.let {
                        emit(DataResource.Error(it.message))
                    }
                }
            }
        } catch (e: ApolloException) {
            emit(DataResource.Error(e.localizedMessage ?: "An unexpected error occurred"))
        } catch(e: HttpException) {
            emit(DataResource.Error(e.localizedMessage ?: "An unexpected error occurred"))
        } catch(e: IOException) {
            emit(DataResource.Error("Couldn't reach server. Check your internet connection."))
        }
    }

    private fun taskList(apolloResponse: ApolloResponse<AllTasksListQuery.Data>): List<Task> {
        return apolloResponse.data?.allTasks?.filterNotNull()
            ?.map { task -> TaskAdapter.toDomain(task) } ?: kotlin.run { emptyList() }
    }

}