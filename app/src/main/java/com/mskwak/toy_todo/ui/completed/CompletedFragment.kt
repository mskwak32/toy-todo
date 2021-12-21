package com.mskwak.toy_todo.ui.completed

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.mskwak.toy_todo.R
import com.mskwak.toy_todo.databinding.FragmentCompletedBinding
import com.mskwak.toy_todo.ui.home.EDIT_TASK_RESULT_KEY
import com.mskwak.toy_todo.ui.home.HomeViewModel
import com.mskwak.toy_todo.ui.home.ListItemDecoration
import com.mskwak.toy_todo.ui.home.TaskAdapter
import com.mskwak.toy_todo.util.setupSnackbar
import com.mskwak.toy_todo.util.showSnackbar
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class CompletedFragment : Fragment() {
    private lateinit var binding: FragmentCompletedBinding

    @Inject
    internal lateinit var viewModelAssistedFactory: HomeViewModel.HomeViewModelAssistedFactory
    private val viewModel by viewModels<HomeViewModel> {
        HomeViewModel.provideFactory(viewModelAssistedFactory, isActiveTasks = false)
    }
    private val adapter: TaskAdapter by lazy { TaskAdapter(viewModel) }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCompletedBinding.inflate(inflater, container, false).apply {
            viewModel = this@CompletedFragment.viewModel
            lifecycleOwner = viewLifecycleOwner
            recyclerView.addItemDecoration(ListItemDecoration())
            adapter = this@CompletedFragment.adapter
        }
        setHasOptionsMenu(true)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupSnacbar()
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
        findNavController().currentBackStackEntry?.savedStateHandle
            ?.getLiveData<Int>(EDIT_TASK_RESULT_KEY)?.observe(viewLifecycleOwner) {
                view?.showSnackbar(getString(it), Snackbar.LENGTH_LONG)
            }
    }

    private fun setupSnacbar() {
        view?.setupSnackbar(viewLifecycleOwner, viewModel.snacbarMessage, Snackbar.LENGTH_LONG)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.completed_fragment_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return if (item.itemId == R.id.menu_deleteCompletedTask) {
            viewModel.clearCompletedTasks()
            true
        } else {
            false
        }
    }

    private fun navigateToTaskDetail(taskId: Long) {
        val action = CompletedFragmentDirections.actionCompletedFragmentToDetailFragment(taskId)
        findNavController().navigate(action)
    }

}