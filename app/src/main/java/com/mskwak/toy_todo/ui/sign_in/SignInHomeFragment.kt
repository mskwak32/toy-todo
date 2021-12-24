package com.mskwak.toy_todo.ui.sign_in

import androidx.lifecycle.LiveData
import androidx.navigation.fragment.findNavController
import com.mskwak.toy_todo.R
import com.mskwak.toy_todo.databinding.FragmentSigninHomeBinding
import com.mskwak.toy_todo.ui.base.BaseFragment

class SignInHomeFragment : BaseFragment<FragmentSigninHomeBinding>() {
    override val layoutResId = R.layout.fragment_signin_home

    override fun getSnackbarEvent(): LiveData<Int>? = null
    override fun initDataBinding() {
        binding.fragment = this
    }

    fun onEmailSiginIn() {
        val action = SignInHomeFragmentDirections.actionSiginInHomeFragmentToSignInFragment()
        findNavController().navigate(action)
    }

    //TODO fun onGoogleSignIn()
}