package com.example.adegacaze

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.adegacaze.databinding.FragmentUserProfileBinding
import com.example.adegacaze.databinding.FragmentUserRegisterBinding


class UserProfileFragment : Fragment() {
    lateinit var binding: FragmentUserProfileBinding;
    lateinit var ctx: Context;



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentUserProfileBinding.inflate(inflater, container, false)
        abrirMenuEndereco()
        sair()

        if (container != null)
            ctx = container.context;

        return binding.root
    }

    private fun sair() {
        binding.buttonSair.setOnClickListener {
            removeUserPreferences(ctx);

            val intent = Intent(activity, MainActivity::class.java);

            startActivity(intent);

        }
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