package com.mskwak.toy_todo.ui.sign_in.password

import androidx.fragment.app.viewModels
import androidx.lifecycle.LiveData
import androidx.navigation.fragment.findNavController
import com.mskwak.toy_todo.R
import com.mskwak.toy_todo.databinding.FragmentRecoverPasswordBinding
import com.mskwak.toy_todo.ui.base.BaseFragment

class RecoverPasswordFragment : BaseFragment<FragmentRecoverPasswordBinding>() {
    override val layoutResId = R.layout.fragment_recover_password
    private val viewModel by viewModels<RecoverPasswordViewModel>()

    override fun getSnackbarEvent(): LiveData<Int> = viewModel.snackbarMessage

    override fun initDataBinding() {
        binding.viewModel = viewModel
    }

    override fun initState() {
        viewModel.emailErrorMessage.observe(viewLifecycleOwner) { stringRes ->
            binding.inputLayout.error = stringRes?.let { getString(it) }
        }
        viewModel.onSendEvent.observe(viewLifecycleOwner) { email ->
            showSendAlertDialog(email)
        }
    }

    private fun showSendAlertDialog(email: String) {
        val action = RecoverPasswordFragmentDirections.showRecoverPasswordAlertDialog(email)
        findNavController().navigate(action)
    }
}