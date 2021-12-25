package com.mskwak.toy_todo.ui.sign_in

import android.content.Intent
import androidx.fragment.app.viewModels
import androidx.lifecycle.LiveData
import androidx.navigation.fragment.findNavController
import com.mskwak.toy_todo.R
import com.mskwak.toy_todo.databinding.FragmentSigninBinding
import com.mskwak.toy_todo.ui.base.BaseFragment
import com.mskwak.toy_todo.ui.main.MainActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SignInFragment : BaseFragment<FragmentSigninBinding>() {
    override val layoutResId = R.layout.fragment_signin
    private val viewModel by viewModels<SignInViewModel>()

    override fun initDataBinding() {
        binding.viewModel = this.viewModel
    }

    override fun initState() {
        viewModel.onNextEvent.observe(viewLifecycleOwner) {
            navigateToPasswordFragment()
        }
        viewModel.onSaveNewUserEvent.observe(viewLifecycleOwner) {
            navigateToMainActivity()
        }
        viewModel.emailErrorMessage.observe(viewLifecycleOwner) { stringRes ->
            binding.emailInputLayout.error = stringRes?.let { getString(it) }
        }
        viewModel.pwErrorMessage.observe(viewLifecycleOwner) { stringRes ->
            binding.passwordInputLayout.error = stringRes?.let { getString(it) }
        }
    }

    override fun getSnackbarEvent(): LiveData<Int> = viewModel.snackbarMessage

    private fun navigateToPasswordFragment() {
        val action = SignInFragmentDirections.signInFragmentToPasswordFragment()
        findNavController().navigate(action)
    }

    private fun navigateToMainActivity() {
        val intent = Intent(requireActivity(), MainActivity::class.java)
        startActivity(intent)
    }
}