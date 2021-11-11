package com.example.adegacaze

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.adegacaze.databinding.FragmentWelcomeBinding
import com.example.adegacaze.view.login.LoginFragment


class WelcomeFragment : Fragment() {

    lateinit var binding: FragmentWelcomeBinding;
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentWelcomeBinding.inflate(inflater, container, false)
        cadastrar()
        entrar()
        return binding.root
    }


    private fun cadastrar() {
        binding.buttonSignUp.setOnClickListener {

            val signUpFrag = UserRegisterFragment.newInstance(null);
            parentFragmentManager.beginTransaction()
                .replace(R.id.container, signUpFrag)
                .addToBackStack(null)
                .commit()


        }
    }

    private fun entrar() {
        binding.buttonLogin.setOnClickListener {

            val loginFrag = LoginFragment.newInstance();
            parentFragmentManager.beginTransaction()
                .replace(R.id.container, loginFrag)
                .addToBackStack(null)
                .commit()

        }
    }

    companion object {
        @JvmStatic
        fun newInstance() = WelcomeFragment()
    }
}