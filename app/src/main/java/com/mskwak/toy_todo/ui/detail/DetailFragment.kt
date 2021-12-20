package com.mskwak.toy_todo.ui.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.snackbar.Snackbar
import com.mskwak.toy_todo.R
import com.mskwak.toy_todo.databinding.FragmentDetailBinding
import com.mskwak.toy_todo.ui.home.EDIT_TASK_RESULT_KEY
import com.mskwak.toy_todo.util.setupSnackbar
import com.mskwak.toy_todo.util.showSnackbar
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class DetailFragment : Fragment() {
    private val args by navArgs<DetailFragmentArgs>()
    private lateinit var binding: FragmentDetailBinding

    @Inject
    internal lateinit var viewModelAssistedFactory: DetailViewModel.DetailViewModelAssistedFactory
    private val viewModel by viewModels<DetailViewModel> {
        DetailViewModel.provideFactory(viewModelAssistedFactory, args.taskId)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDetailBinding.inflate(inflater, container, false).apply {
            viewModel = this@DetailFragment.viewModel
            lifecycleOwner = this@DetailFragment.viewLifecycleOwner
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupSnackbar()
        initObserver()
    }

    private fun setupSnackbar() {
        view?.setupSnackbar(viewLifecycleOwner, viewModel.snackbarMessage, Snackbar.LENGTH_LONG)
    }

    private fun initObserver() {
        viewModel.onDeleteEvent.observe(viewLifecycleOwner) {
            finishWithResult(R.string.message_task_deleted)
        }
        viewModel.onEditEvent.observe(viewLifecycleOwner) {
            navigateToEditFragment()
        }
        viewModel.onCompleteEvent.observe(viewLifecycleOwner) {
            finishWithResult(R.string.message_marked_complete)
        }
        findNavController().currentBackStackEntry?.savedStateHandle
            ?.getLiveData<Int>(EDIT_TASK_RESULT_KEY)?.observe(viewLifecycleOwner) {
                view?.showSnackbar(getString(it), Snackbar.LENGTH_LONG)
            }
    }

    private fun finishWithResult(@StringRes stringId: Int) {
        with(findNavController()) {
            previousBackStackEntry?.savedStateHandle?.set(EDIT_TASK_RESULT_KEY, stringId)
            popBackStack()
        }
    }

    private fun navigateToEditFragment() {
        val action =
            DetailFragmentDirections.actionDetailFragmentToEditFragment(args.taskId.toString())
        findNavController().navigate(action)
    }
}