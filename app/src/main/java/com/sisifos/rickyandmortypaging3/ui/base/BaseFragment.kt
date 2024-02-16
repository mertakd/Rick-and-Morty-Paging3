package com.sisifos.rickyandmortypaging3.ui.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController
import androidx.viewbinding.ViewBinding
import com.sisifos.rickyandmortypaging3.viewBinding


abstract class BaseFragment<T : ViewBinding>(factory: (LayoutInflater) -> T) : Fragment() {

    val binding: T by viewBinding(factory)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        callInitialViewModelFunctions()
        observeUi()
    }

    open fun observeUi() = Unit

    open fun callInitialViewModelFunctions() = Unit

    open fun navigate(action: NavDirections) {
        findNavController().navigate(action)
    }
}