package com.mskwak.toy_todo.ui.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.mskwak.toy_todo.R
import com.mskwak.toy_todo.databinding.DialogRecoverPasswordAlertBinding

class RecoverPasswordAlertDialog : BottomSheetDialogFragment() {
    private var binding: DialogRecoverPasswordAlertBinding? = null
    private val args by navArgs<RecoverPasswordAlertDialogArgs>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DialogRecoverPasswordAlertBinding.inflate(inflater, container, false).apply {
            lifecycleOwner = viewLifecycleOwner
            fragment = this@RecoverPasswordAlertDialog
        }
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initDescription()
    }

    private fun initDescription() {
        val email = args.email
        binding?.description?.text = getString(R.string.check_your_email_description, email)
    }

    fun onConfirm() {
        dismiss()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}