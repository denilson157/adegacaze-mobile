package com.example.adegacaze.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.adegacaze.databinding.FragmentOrderBinding

class OrderFragment : Fragment() {
    lateinit var binding: FragmentOrderBinding;

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentOrderBinding.inflate(inflater, container, false)
        return binding.root
    }



    companion object {
        @JvmStatic
        fun newInstance(id: Int?) = OrderFragment()
    }




}