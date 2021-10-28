package com.example.adegacaze

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.adegacaze.databinding.FragmentUserProfileBinding
import com.example.adegacaze.databinding.FragmentUserRegisterBinding


class UserProfileFragment : Fragment() {
    lateinit var binding: FragmentUserProfileBinding;


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentUserProfileBinding.inflate(inflater, container, false)
        abrirMenuEndereco()
        return binding.root
    }

    private fun abrirMenuEndereco() {
        binding.cardEndereco.setOnClickListener {


            val addressFrag = ListAddressFragment.newInstance();
            parentFragmentManager.beginTransaction()
                .replace(R.id.container, addressFrag)
                .addToBackStack(null)
                .commit()
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() = UserProfileFragment()
    }
}