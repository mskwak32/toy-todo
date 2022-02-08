package com.mskwak.toy_todo.ui.sign_in

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.viewModels
import androidx.lifecycle.LiveData
import androidx.navigation.fragment.findNavController
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.material.snackbar.Snackbar
import com.mskwak.toy_todo.R
import com.mskwak.toy_todo.databinding.FragmentSigninHomeBinding
import com.mskwak.toy_todo.ui.base.BaseFragment
import com.mskwak.toy_todo.ui.main.MainActivity
import com.mskwak.toy_todo.util.showSnackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SignInHomeFragment : BaseFragment<FragmentSigninHomeBinding>() {
    override val layoutResId = R.layout.fragment_signin_home
    private lateinit var googleIdTokenLauncher: ActivityResultLauncher<Intent>
    private val viewModel by viewModels<SignInHomeViewModel>()

    override fun getSnackbarEvent(): LiveData<Int> = viewModel.snackbarMessage
    override fun initDataBinding() {
        binding?.fragment = this
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initGoogleSignInActivity()
    }

    override fun initState() {
        viewModel.onCredentialSignInEvent.observe(viewLifecycleOwner) {
            navigateToMainActivity()
        }
    }

    fun onEmailSignIn() {
        val action = SignInHomeFragmentDirections.actionSiginInHomeFragmentToSignInFragment()
        findNavController().navigate(action)
    }

    fun onGoogleSignIn() {
        val signInIntent = getGoogleSignInIntent()
        googleIdTokenLauncher.launch(signInIntent)
    }

    private fun getGoogleSignInIntent(): Intent {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.firebase_web_client_id))
            .requestEmail()
            .build()
        val googleSignInClient = GoogleSignIn.getClient(requireContext(), gso)
        return googleSignInClient.signInIntent
    }

    private fun initGoogleSignInActivity() {
        googleIdTokenLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == RESULT_OK) {
                    val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
                    try {
                        val account = task.getResult(ApiException::class.java)!!
                        Log.d(TAG, "firebase auth with google: ${account.id}")
                        viewModel.firebaseAuthWithGoogle(account.idToken!!)
                    } catch (e: ApiException) {
                        Log.w(TAG, "Google sign in: failed", e)
                        view?.showSnackbar(
                            getString(R.string.message_authentication_fail),
                            Snackbar.LENGTH_SHORT
                        )
                    }
                }
            }
    }

    private fun navigateToMainActivity() {
        val intent = Intent(requireActivity(), MainActivity::class.java)
        startActivity(intent)
    }

    companion object {
        private val TAG = SignInHomeFragment::class.simpleName
    }
}