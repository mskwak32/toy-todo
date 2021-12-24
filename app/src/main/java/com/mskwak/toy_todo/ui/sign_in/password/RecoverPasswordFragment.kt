package com.mskwak.toy_todo.ui.sign_in.password

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.mskwak.toy_todo.databinding.FragmentRecoverPasswordBinding

class RecoverPasswordFragment : Fragment() {
    private lateinit var binding: FragmentRecoverPasswordBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRecoverPasswordBinding.inflate(inflater, container, false)
        return binding.root
    }
}