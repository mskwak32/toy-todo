package com.mskwak.toy_todo.ui.sign_in.password

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.LiveData
import androidx.navigation.fragment.findNavController
import com.mskwak.toy_todo.R
import com.mskwak.toy_todo.databinding.FragmentPasswordBinding
import com.mskwak.toy_todo.ui.base.BaseFragment
import com.mskwak.toy_todo.ui.main.MainActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PasswordFragment : BaseFragment<FragmentPasswordBinding>() {
    override val layoutResId = R.layout.fragment_password
    private val viewModel by viewModels<PasswordViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPasswordBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun getSnackbarEvent(): LiveData<Int> = viewModel.snackbarMessage

    override fun initDataBinding() {
        binding.viewModel = viewModel
    }

    override fun initState() {
        viewModel.signInEvent.observe(viewLifecycleOwner) {
            navigateToMainActivity()
        }
        viewModel.recoverPwEvent.observe(viewLifecycleOwner) {
            navigateToRecoverPw()
        }
        viewModel.pwErrorMessage.observe(viewLifecycleOwner) { stringRes ->
            binding.inputLayout.error = stringRes?.let { getString(it) }
        }
    }

    private fun navigateToMainActivity() {
        val intent = Intent(requireActivity(), MainActivity::class.java)
        startActivity(intent)
    }

    private fun navigateToRecoverPw() {
        val action = PasswordFragmentDirections.passwordFragmentToRecoverPasswordFragment()
        findNavController().navigate(action)
    }
}