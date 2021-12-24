package com.mskwak.toy_todo.ui.home

import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.fragment.app.viewModels
import androidx.lifecycle.LiveData
import androidx.navigation.fragment.findNavController
import com.mskwak.toy_todo.R
import com.mskwak.toy_todo.databinding.FragmentHomeBinding
import com.mskwak.toy_todo.ui.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding>() {
    override val layoutResId = R.layout.fragment_home

    @Inject
    internal lateinit var viewModelAssistedFactory: HomeViewModel.HomeViewModelAssistedFactory
    private val viewModel by viewModels<HomeViewModel> {
        HomeViewModel.provideFactory(viewModelAssistedFactory, isActiveTasks = true)
    }
    private val adapter: TaskAdapter by lazy { TaskAdapter(viewModel) }

    override fun getSnackbarEvent(): LiveData<Int> = viewModel.snacbarMessage

    override fun initDataBinding() {
        binding.viewModel = viewModel
        binding.adapter = adapter
        binding.recyclerView.addItemDecoration(ListItemDecoration())
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
        inflater.inflate(R.menu.main_fragment_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return if (item.itemId == R.id.menu_addTask) {
            navigateToAddNewTask()
            true
        } else {
            false
        }
    }

    private fun navigateToAddNewTask() {
        val title = getString(R.string.new_task)
        val action = HomeFragmentDirections.actionHomeFragmentToEditFragment(null, title)
        findNavController().navigate(action)
    }

    private fun navigateToTaskDetail(taskId: Long) {
        val action = HomeFragmentDirections.actionHomeFragmentToDetailFragment(taskId)
        findNavController().navigate(action)
    }
}