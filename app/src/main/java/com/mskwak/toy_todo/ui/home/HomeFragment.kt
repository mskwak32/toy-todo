package com.mskwak.toy_todo.ui.home

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.mskwak.toy_todo.R
import com.mskwak.toy_todo.databinding.FragmentHomeBinding
import com.mskwak.toy_todo.util.setupSnackbar
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding

    @Inject
    internal lateinit var viewModelAssistedFactory: HomeViewModel.HomeViewModelAssistedFactory
    private val viewModel by viewModels<HomeViewModel> {
        HomeViewModel.provideFactory(viewModelAssistedFactory, isActiveTasks = true)
    }
    private val adapter: TaskAdapter by lazy { TaskAdapter(viewModel) }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false).apply {
            viewModel = this@HomeFragment.viewModel
            lifecycleOwner = this@HomeFragment.viewLifecycleOwner
            recyclerView.addItemDecoration(ListItemDecoration())
            adapter = this@HomeFragment.adapter
        }
        setHasOptionsMenu(true)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initObserver()
        setupSnackbar()
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

    private fun setupSnackbar() {
        view?.setupSnackbar(viewLifecycleOwner, viewModel.snacbarMessage, Snackbar.LENGTH_SHORT)
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