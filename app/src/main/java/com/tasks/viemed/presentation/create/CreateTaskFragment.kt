package com.tasks.viemed.presentation.create

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.coroutineScope
import com.challenge.mozper.util.hideKeyboard
import com.tasks.viemed.R
import com.tasks.viemed.databinding.FragmentCreateTaskBinding
import com.tasks.viemed.util.showToast
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CreateTaskFragment : Fragment(R.layout.fragment_create_task), CreateTaskScreenEventHandler {

    private var _binding: FragmentCreateTaskBinding? = null
    private val binding by lazy { _binding!! }
    private val createTaskViewModel: CreateTaskViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCreateTaskBinding.inflate(inflater, container, false)
        binding.eventHandler = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeViewModel()
    }

    override fun onCreateTaskButtonClicked() {
        hideKeyboard()

        if (binding.taskTitle.text.isEmpty()) {
            binding.taskTitle.error = getString(R.string.task_title_error)
        }
        createTaskViewModel.addNewTask(binding.taskTitle.text.toString(), binding.taskNote.text.toString())

        binding.taskNote.isEnabled = false
        binding.taskTitle.isEnabled = false
    }

    private fun observeViewModel() {
        createTaskViewModel.taskScreenState.observe(viewLifecycleOwner) { screenState ->
            when {
                screenState.isLoading -> binding.taskLoadingSprinner.visibility = VISIBLE
                screenState.task != null -> {
                    hideLoaderSpinner()
                    viewLifecycleOwner.lifecycle.coroutineScope.launch {
                        delay(600)
                        showToast("A new task was created")
                        requireActivity().onBackPressed()
                    }
                }
                screenState.error.isNotEmpty() ->  {
                    hideLoaderSpinner()
                    showToast(screenState.error)
                }
            }
        }
    }

    private fun hideLoaderSpinner() {
        binding.taskLoadingSprinner.visibility = GONE
    }

}