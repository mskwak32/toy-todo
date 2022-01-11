package com.mskwak.toy_todo.ui.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import com.google.android.material.snackbar.Snackbar
import com.mskwak.toy_todo.util.setupSnackbar

abstract class BaseFragment<VB : ViewDataBinding> : Fragment() {
    protected var binding: VB? = null
    abstract val layoutResId: Int

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, layoutResId, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    open fun initState() {}
    abstract fun initDataBinding()
    abstract fun getSnackbarEvent(): LiveData<Int>?

    private fun setupSnackbar() {
        getSnackbarEvent()?.let {
            view?.setupSnackbar(viewLifecycleOwner, it, Snackbar.LENGTH_SHORT)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initDataBinding()
        initState()
        setupSnackbar()
    }

<<<<<<< HEAD
    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
=======
    //TODO memory leak 해결하기
>>>>>>> 79ffabb (firebase firestore 추가)
}