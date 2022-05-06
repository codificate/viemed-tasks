package com.tasks.viemed.presentation.list

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tasks.viemed.base.DataResource
import com.tasks.viemed.domain.use_cases.DeleteTaskUseCase
import com.tasks.viemed.domain.use_cases.FetchAllTasksUseCase
import com.tasks.viemed.domain.use_cases.GenerateTokenUseCase
import com.tasks.viemed.domain.use_cases.UpdateTaskStatusUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class ListTaskViewModel @Inject constructor(
    private val generateTokenUseCase: GenerateTokenUseCase,
    private val fetchAllTasksUseCase: FetchAllTasksUseCase,
    private val updateTaskStatusUseCase: UpdateTaskStatusUseCase,
    private val deleteTaskUseCase: DeleteTaskUseCase
) : ViewModel() {

    private val _mutableListTasksScreenState = MutableLiveData(ListTaskScreenState())
    val tasksScreenState: LiveData<ListTaskScreenState> = _mutableListTasksScreenState

    private val _mutableTaskUpdatedScreenState = MutableLiveData(TaskUpdatedScreenState())
    val taskUpdatedScreenState: LiveData<TaskUpdatedScreenState> = _mutableTaskUpdatedScreenState

    private val _mutableTaskDeletedScreenState = MutableLiveData(TaskDeletedScreenState())
    val taskDeletedScreenState: LiveData<TaskDeletedScreenState> = _mutableTaskDeletedScreenState

    private lateinit var token: String

    init {
        generateTokenUseCase().onEach { tokenResponse ->
            if (tokenResponse is DataResource.Success) {
                tokenResponse.data?.let {
                    token = it
                    retrieveTasks()
                }
            }
        }.launchIn(viewModelScope)
    }

    fun updateStatus(taskId: String, newStatus: Boolean) {
        updateTaskStatusUseCase(token, taskId, newStatus).onEach { taskUpdatedResponse ->
            when (taskUpdatedResponse) {
                is DataResource.Error -> {
                    _mutableTaskUpdatedScreenState.postValue(
                        TaskUpdatedScreenState(
                            error = taskUpdatedResponse.message ?: "An unexpected error occurred"
                        )
                    )
                }
                is DataResource.Loading -> {
                    _mutableTaskUpdatedScreenState.postValue(TaskUpdatedScreenState(isLoading = true))
                }
                is DataResource.Success -> {
                    _mutableTaskUpdatedScreenState.postValue(TaskUpdatedScreenState(task = taskUpdatedResponse.data))
                }
            }
        }.launchIn(viewModelScope)
    }

    fun tryDeleteTask(taskId: String) {
        deleteTaskUseCase(token, taskId).onEach { taskDeletedResponse ->
            when (taskDeletedResponse) {
                is DataResource.Success -> {
                    if (taskDeletedResponse.data != null && taskDeletedResponse.data == true) {
                        _mutableTaskDeletedScreenState.postValue(
                            TaskDeletedScreenState(taskIdWasDeleted = taskId)
                        )
                    }
                }
                is DataResource.Loading -> {
                    _mutableTaskDeletedScreenState.postValue(TaskDeletedScreenState(isLoading = true))
                }
                is DataResource.Error -> {
                    _mutableTaskDeletedScreenState.postValue(
                        TaskDeletedScreenState(
                            error = taskDeletedResponse.message ?: "An unexpected error occurred"
                        )
                    )
                }
            }

        }.launchIn(viewModelScope)
    }

    fun retrieveTasks() {
        if (this::token.isInitialized) {
            fetchAllTasksUseCase(token).onEach { result ->
                when (result) {
                    is DataResource.Error -> {
                        _mutableListTasksScreenState.postValue(
                            ListTaskScreenState(
                                error = result.message ?: "An unexpected error occurred"
                            )
                        )
                    }
                    is DataResource.Loading -> {
                        _mutableListTasksScreenState.postValue(ListTaskScreenState(isLoading = true))
                    }
                    is DataResource.Success -> {
                        _mutableListTasksScreenState.postValue(result.data?.let {
                            ListTaskScreenState(
                                tasks = it
                            )
                        })
                    }
                }
            }.launchIn(viewModelScope)
        }
    }
}