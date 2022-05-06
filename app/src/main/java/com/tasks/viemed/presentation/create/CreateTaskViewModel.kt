package com.tasks.viemed.presentation.create

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tasks.viemed.base.DataResource
import com.tasks.viemed.domain.use_cases.CreateTaskUseCase
import com.tasks.viemed.domain.use_cases.GenerateTokenUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class CreateTaskViewModel @Inject constructor(
    private val generateTokenUseCase: GenerateTokenUseCase,
    private val createTaskUseCase: CreateTaskUseCase
) : ViewModel() {

    private val _mutableTaskState = MutableLiveData(CreateTasScreenState())
    val taskScreenState: LiveData<CreateTasScreenState> = _mutableTaskState

    private lateinit var token: String

    init {
        generateTokenUseCase().onEach { tokenResponse ->
            if (tokenResponse is DataResource.Success) {
                tokenResponse.data?.let {
                    token = it
                }
            }
        }.launchIn(viewModelScope)
    }

    fun addNewTask(title: String, note: String) {
        createTaskUseCase(token, title, note).onEach { result ->
            when (result) {
                is DataResource.Error -> {
                    _mutableTaskState.postValue(
                        CreateTasScreenState(
                            error = result.message ?: "An unexpected error occurred"
                        )
                    )
                }
                is DataResource.Loading -> {
                    _mutableTaskState.postValue(CreateTasScreenState(isLoading = true))
                }
                is DataResource.Success -> {
                    _mutableTaskState.postValue(CreateTasScreenState(task = result.data))
                }
            }
        }.launchIn(viewModelScope)
    }

}