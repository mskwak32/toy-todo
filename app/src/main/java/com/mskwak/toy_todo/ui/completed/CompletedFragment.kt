package com.mskwak.toy_todo.ui.completed

import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.PopupMenu
import androidx.fragment.app.viewModels
import androidx.lifecycle.LiveData
import androidx.navigation.fragment.findNavController
import com.mskwak.toy_todo.R
import com.mskwak.toy_todo.databinding.FragmentCompletedBinding
import com.mskwak.toy_todo.ui.base.BaseFragment
import com.mskwak.toy_todo.ui.home.HomeViewModel
import com.mskwak.toy_todo.ui.home.ListItemDecoration
import com.mskwak.toy_todo.ui.home.TaskAdapter
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class CompletedFragment : BaseFragment<FragmentCompletedBinding>() {
    override val layoutResId = R.layout.fragment_completed

    @Inject
    internal lateinit var viewModelAssistedFactory: HomeViewModel.HomeViewModelAssistedFactory
    private val viewModel by viewModels<HomeViewModel> {
        HomeViewModel.provideFactory(viewModelAssistedFactory, isActiveTasks = false)
    }
    private val adapter: TaskAdapter by lazy { TaskAdapter(viewModel) }

    override fun getSnackbarEvent(): LiveData<Int> = viewModel.snacbarMessage

    override fun initDataBinding() {
        binding?.viewModel = viewModel
        binding?.adapter = adapter
        binding?.recyclerView?.addItemDecoration(ListItemDecoration())
    }

    override fun initState() {
        setHasOptionsMenu(true)
        initObserver()
    }

    private fun initObserver() {
        viewModel.tasks.observe(viewLifecycleOwner) {
            adapter.submitList(it)
            viewModel.isEmptyList.value = it.isEmpty()
        }
        viewModel.openDetailEvent.observe(viewLifecycleOwner) {
            navigateToTaskDetail(it)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.completed_fragment_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return if (item.itemId == R.id.menu_completedFragment) {
            showMoreMenu()
            true
        } else {
            false
        }
    }

    private fun navigateToTaskDetail(taskId: Long) {
        val action = CompletedFragmentDirections.actionCompletedFragmentToDetailFragment(taskId)
        findNavController().navigate(action)
    }

    private fun showMoreMenu() {
        val view = activity?.findViewById<View>(R.id.menu_completedFragment) ?: return
        PopupMenu(requireContext(), view).run {
            menuInflater.inflate(R.menu.completed_fragment_menu_task, menu)

            setOnMenuItemClickListener {
                if (it.itemId == R.id.menu_clearCompletedTask) {
                    viewModel.clearCompletedTasks()
                }
                true
            }
            show()
        }
    }

}