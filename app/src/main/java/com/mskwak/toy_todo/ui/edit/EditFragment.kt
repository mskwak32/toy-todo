package com.mskwak.toy_todo.ui.edit

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.snackbar.Snackbar
import com.mskwak.toy_todo.databinding.FragmentEditBinding
import com.mskwak.toy_todo.ui.home.EDIT_TASK_RESULT_KEY
import com.mskwak.toy_todo.util.setupSnackbar
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class EditFragment : Fragment() {
    private val args: EditFragmentArgs by navArgs()
    private lateinit var binding: FragmentEditBinding
    private lateinit var imm: InputMethodManager

    @Inject
    internal lateinit var viewModelFactory: EditViewModel.EditViewModelAssistedFactory
    private val viewModel by viewModels<EditViewModel> {
        EditViewModel.provideFactory(viewModelFactory, args.taskId?.toLong())
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentEditBinding.inflate(inflater, container, false).apply {
            viewModel = this@EditFragment.viewModel
            lifecycleOwner = viewLifecycleOwner
            fragment = this@EditFragment
        }

        imm = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

        return binding.root
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupSnackbar()
        initObserver()
        view.setOnTouchListener { _, _ ->
            hideKeypad()
            false
        }
    }

    private fun setupSnackbar() {
        view?.setupSnackbar(viewLifecycleOwner, viewModel.snackbarMessage, Snackbar.LENGTH_SHORT)
    }

    fun onCancel() {
        findNavController().popBackStack()
    }

    private fun initObserver() {
        viewModel.onSaveEvent.observe(viewLifecycleOwner) {
            finishWithResult(it)
        }
        viewModel.onUpdateEvent.observe(viewLifecycleOwner) {
            finishWithResult(it)
        }
    }

    private fun finishWithResult(@StringRes stringId: Int) {
        with(findNavController()) {
            previousBackStackEntry?.savedStateHandle
                ?.set(EDIT_TASK_RESULT_KEY, stringId)
            popBackStack()
        }
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