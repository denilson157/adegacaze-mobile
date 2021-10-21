package com.example.adegacaze

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doOnTextChanged
import com.example.adegacaze.databinding.FragmentLoginBinding


class LoginFragment : Fragment() {
    lateinit var binding: FragmentLoginBinding;

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLoginBinding.inflate(inflater, container, false)
        login()
        removerErros()
        return binding.root
    }

    private fun login() {
        binding.buttonSignIn.setOnClickListener() {
            if (validarCampos()) {
                val email = binding.editEmail.text;
                val senha = binding.editPassword.text;
            }
        }
    }

    private fun validarCampos(): Boolean {
        var semErros = true;

        if (binding.editEmail.text.isNullOrEmpty()) {
            binding.editEmailInputLayout.error = "Informe um email";
            binding.editEmail.requestFocus()
            semErros = false;
        }

        if (binding.editPassword.text.isNullOrEmpty()) {
            binding.editPasswordInputLayout.error = "Informe uma senha";
            binding.editPassword.requestFocus()
            semErros = false;
        }



        return semErros;
    }

    private fun removerErros() {
        binding.editEmail.doOnTextChanged() { _, _, _, _ ->
            binding.editEmailInputLayout.isErrorEnabled = false
        }
        binding.editPassword.doOnTextChanged() { _, _, _, _ ->
            binding.editPasswordInputLayout.isErrorEnabled = false
        }

    }

    companion object {
        @JvmStatic
        fun newInstance() = LoginFragment()
    }
}