package com.example.adegacaze.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.adegacaze.R
import com.example.adegacaze.UserProfileFragment
import com.example.adegacaze.databinding.FragmentProductBinding

class ProductFragment : Fragment() {
    lateinit var binding: FragmentProductBinding;

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentProductBinding.inflate(inflater, container, false)

        return binding.root
    }
    companion object {
        @JvmStatic
        fun newInstance() = ProductFragment()
    }
}