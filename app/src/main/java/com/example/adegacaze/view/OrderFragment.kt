package com.example.adegacaze.view

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.adegacaze.AdressUserFragment
import com.example.adegacaze.R
import com.example.adegacaze.databinding.FragmentAdressUserBinding
import com.example.adegacaze.databinding.FragmentOrderBinding
import com.example.adegacaze.model.Address
import com.example.adegacaze.service.IAddressService
import com.example.adegacaze.service.getService
import com.google.android.material.snackbar.Snackbar
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

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