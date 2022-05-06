package com.tasks.viemed.presentation.list

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.tasks.viemed.R
import com.tasks.viemed.databinding.FragmentListTaskBinding
import com.tasks.viemed.domain.model.Task
import com.tasks.viemed.util.showToast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ListTaskFragment : Fragment(R.layout.fragment_list_task), ListTaskScreenEventHandler,
    TaskItemEventHandler {

    private var _binding: FragmentListTaskBinding? = null
    private val binding by lazy { _binding!! }
    private val tasksViewModel: ListTaskViewModel by viewModels()
    private val adapter = TaskListAdapter(this)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentListTaskBinding.inflate(inflater, container, false)
        binding.eventHandler = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        tasksViewModel.retrieveTasks()
        binding.taskList.setHasFixedSize(true)
        binding.taskList.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.taskList.adapter = adapter
        observeViewModel()
    }

    override fun onCreateNewTaskButtonClicked() {
        findNavController().navigate(ListTaskFragmentDirections.actionListTasksFragmentToCreateNewTaskFragment())
    }

    override fun onMoreActionsIconClicked(task: Task) {
        AlertDialog.Builder(requireContext()).apply {
            setTitle("Remove task")
            setMessage("Are you sure to remove this task?")
            setPositiveButton("Yes") { dialog, _ ->
                tasksViewModel.tryDeleteTask(task.id)
                dialog.dismiss()
            }
            setNegativeButton("No") { dialog, _ ->
                dialog.dismiss()
            }
        }.show()
    }

    override fun onStatusDoneClicked(task: Task) {
        tasksViewModel.updateStatus(task.id, !task.isDone)
    }

    override fun onStop() {
        super.onStop()
        tasksViewModel.taskDeletedScreenState.removeObservers(viewLifecycleOwner)
        tasksViewModel.taskUpdatedScreenState.removeObservers(viewLifecycleOwner)
    }

    private fun observeViewModel() {
        observeListTask()

        observeTaskDeleted()

        observeTaskUpdated()
    }

    private fun observeListTask() {
        tasksViewModel.tasksScreenState.observe(viewLifecycleOwner) { screenState ->
            when {
                screenState.tasks.isNullOrEmpty() -> {
                    hideLoaderSpinner()
                }

                !screenState.tasks.isNullOrEmpty() -> screenState.tasks.let { tasks ->
                    adapter.setTasksList(tasks)
                    hideLoaderSpinner()
                }

                screenState.error.isNotEmpty() -> {
                    hideLoaderSpinner()
                    showToast(screenState.error)
                }

                screenState.isLoading -> binding.taskLoadingSprinner.visibility = VISIBLE
            }
        }
    }

    private fun observeTaskDeleted() {
        tasksViewModel.taskDeletedScreenState.observe(viewLifecycleOwner) { screenState ->
            when {
                screenState.error.isNotEmpty() -> {
                    hideLoaderSpinner()
                    showToast(screenState.error)
                }

                screenState.isLoading -> binding.taskLoadingSprinner.visibility = VISIBLE

                screenState.taskIdWasDeleted != null -> {
                    adapter.removeTask(screenState.taskIdWasDeleted)
                    hideLoaderSpinner()
                }
            }
        }
    }

    private fun observeTaskUpdated() {
        tasksViewModel.taskUpdatedScreenState.observe(viewLifecycleOwner) { screenState ->
            when {
                screenState.error.isNotEmpty() -> {
                    hideLoaderSpinner()
                    showToast(screenState.error)
                }

                screenState.isLoading -> binding.taskLoadingSprinner.visibility = VISIBLE

                screenState.task != null -> {
                    adapter.updateTask(screenState.task)
                    hideLoaderSpinner()
                }
            }
        }
    }

    private fun hideLoaderSpinner() {
        binding.taskLoadingSprinner.visibility = GONE
    }
}