package com.michelbarbosa.magictables.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.michelbarbosa.magictables.data.cache.AppCache
import com.michelbarbosa.magictables.databinding.FragmentPresentationBinding
import com.michelbarbosa.magictables.utils.closeApp
import com.michelbarbosa.magictables.utils.setOnSingleClickListener
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PresentationFragment: Fragment() {
    private lateinit var binding: FragmentPresentationBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPresentationBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupWidgets()
    }

    private fun setupWidgets() = with(binding) {
        btnClose.setOnSingleClickListener {
            closeApp()
        }
        btnNext.setOnSingleClickListener {
            forwardScreen()
        }
        btnStart.setOnSingleClickListener {
            AppCache.wipeOut()
            forwardScreen()
        }
    }

    private fun forwardScreen(){
        findNavController().navigate(
            PresentationFragmentDirections
                .actionPresentationFragmentToHorizontalDataFragment()
        )
    }

}