package com.example.miketoide.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.miketoide.R
import com.example.miketoide.databinding.FragmentGenericErrorBinding
import com.example.miketoide.ui.viewmodels.GenericErrorViewModel

class GenericErrorFragment : Fragment(R.layout.fragment_generic_error)
{
    companion object {
        private val TAG = GenericErrorFragment::class.java.simpleName
    }

    private val vm by viewModels<GenericErrorViewModel>()

    private var _binding: FragmentGenericErrorBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentGenericErrorBinding.inflate(inflater, container, false)
        val view = binding.root
        setTryAgainButton()
        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setTryAgainButton() {
        Log.d(TAG, "setTryAgainButton()")
        binding.tryAgainButton.setOnClickListener {
            Log.d(TAG, "setTryAgainButton().onClick()")
            vm.onTryAgain()
        }
    }
}