package com.mskwak.toy_todo.ui.edit

import android.annotation.SuppressLint
import android.content.Context
import android.view.inputmethod.InputMethodManager
import androidx.annotation.StringRes
import androidx.fragment.app.viewModels
import androidx.lifecycle.LiveData
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.mskwak.toy_todo.R
import com.mskwak.toy_todo.databinding.FragmentEditBinding
import com.mskwak.toy_todo.ui.base.BaseFragment
import com.mskwak.toy_todo.ui.main.MainActivity
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class EditFragment : BaseFragment<FragmentEditBinding>() {
    private val args by navArgs<EditFragmentArgs>()
    private lateinit var imm: InputMethodManager
    override val layoutResId = R.layout.fragment_edit

    @Inject
    internal lateinit var viewModelAssistedFactory: EditViewModel.EditViewModelAssistedFactory
    private val viewModel by viewModels<EditViewModel> {
        EditViewModel.provideFactory(viewModelAssistedFactory, args.taskId?.toLong())
    }

    override fun getSnackbarEvent(): LiveData<Int> = viewModel.snackbarMessage

    override fun initDataBinding() {
        binding?.viewModel = viewModel
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun initState() {
        imm = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        initObserver()
        view?.setOnTouchListener { _, _ ->
            hideKeypad()
            false
        }
    }

    private fun initObserver() {
        viewModel.onAddEvent.observe(viewLifecycleOwner) {
            finishWithMessage(R.string.message_task_added)
        }
        viewModel.onUpdateEvent.observe(viewLifecycleOwner) {
            finishWithMessage(R.string.message_task_saved)
        }
        viewModel.onCancelEvent.observe(viewLifecycleOwner) {
            findNavController().popBackStack()
        }
    }


    private fun finishWithMessage(@StringRes stringId: Int) {
        (activity as? MainActivity)?.showSnacbarMessage(getString(stringId))
        findNavController().popBackStack()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        hideKeypad()
    }

    private fun hideKeypad() {
        if (activity != null && requireActivity().currentFocus != null) {
            imm.hideSoftInputFromWindow(requireActivity().currentFocus?.windowToken, 0)
        }
    }
}