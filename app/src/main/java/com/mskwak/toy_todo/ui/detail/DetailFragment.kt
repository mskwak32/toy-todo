package com.mskwak.toy_todo.ui.detail

import androidx.annotation.StringRes
import androidx.fragment.app.viewModels
import androidx.lifecycle.LiveData
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.mskwak.toy_todo.R
import com.mskwak.toy_todo.databinding.FragmentDetailBinding
import com.mskwak.toy_todo.ui.base.BaseFragment
import com.mskwak.toy_todo.ui.main.MainActivity
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class DetailFragment : BaseFragment<FragmentDetailBinding>() {
    private val args by navArgs<DetailFragmentArgs>()
    override val layoutResId = R.layout.fragment_detail

    @Inject
    internal lateinit var viewModelAssistedFactory: DetailViewModel.DetailViewModelAssistedFactory
    private val viewModel by viewModels<DetailViewModel> {
        DetailViewModel.provideFactory(viewModelAssistedFactory, args.taskId)
    }

    override fun getSnackbarEvent(): LiveData<Int> = viewModel.snackbarMessage

    override fun initDataBinding() {
        binding?.viewModel = viewModel
    }

    override fun initState() {
        viewModel.onDeleteEvent.observe(viewLifecycleOwner) {
            finishWithMessage(R.string.message_task_deleted)
        }
        viewModel.onEditEvent.observe(viewLifecycleOwner) {
            navigateToEditFragment()
        }
    }

    private fun finishWithMessage(@StringRes stringId: Int) {
        (activity as? MainActivity)?.showSnacbarMessage(getString(stringId))
        findNavController().popBackStack()
    }

    private fun navigateToEditFragment() {
        val title = getString(R.string.edit_task)
        val action =
            DetailFragmentDirections.actionDetailFragmentToEditFragment(
                args.taskId.toString(),
                title
            )
        findNavController().navigate(action)
    }
}