package com.mskwak.toy_todo.ui.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.mskwak.toy_todo.databinding.DialogDeleteTaskConfirmBinding
import com.mskwak.toy_todo.ui.detail.DetailViewModel

class DeleteTaskConfirmDialog(private val viewModel: DetailViewModel) :
    BottomSheetDialogFragment() {
    private var binding: DialogDeleteTaskConfirmBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DialogDeleteTaskConfirmBinding.inflate(inflater, container, false).apply {
            lifecycleOwner = viewLifecycleOwner
            viewModel = this@DeleteTaskConfirmDialog.viewModel
            fragment = this@DeleteTaskConfirmDialog
        }
        return binding?.root
    }

    fun onCancel() {
        dismiss()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}