package com.example.adegacaze.view.login

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doOnTextChanged
import com.example.adegacaze.MainActivity
import com.example.adegacaze.R
import com.example.adegacaze.databinding.FragmentLoginBinding
import com.example.adegacaze.databinding.FragmentWelcomeBinding
import com.example.adegacaze.model.Login
import com.example.adegacaze.model.Order
import com.example.adegacaze.model.UsuarioLogin
import com.example.adegacaze.service.API
import com.example.adegacaze.setUserPreferences
import com.example.adegacaze.view.OrdersFragment
import com.google.android.material.snackbar.Snackbar
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

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
                val email = binding.editEmail.text.toString();
                val senha = binding.editPassword.text.toString();

                var objetoLogin = Login(
                    email,
                    senha,
                    "device"
                );

                val callback = object : Callback<UsuarioLogin> {
                    override fun onResponse(
                        call: Call<UsuarioLogin>,
                        response: Response<UsuarioLogin>
                    ) {
                        if (response.isSuccessful) {
                            tratarLoginUsuario(response.body())
                        } else {
                            val error = response.errorBody().toString()

                            Snackbar.make(
                                binding.containerLogin,
                                "Não foi possível fazer login",
                                Snackbar.LENGTH_LONG
                            ).show();

                            Log.e("Erro", error);
                        }
                    }

                    override fun onFailure(call: Call<UsuarioLogin>, t: Throwable) {
                        Snackbar.make(
                            binding.containerLogin,
                            "Não foi possível se conectar com o servidor",
                            Snackbar.LENGTH_LONG
                        ).show();

                        Log.e("Erro", "Falha ao executar serviço", t);
                    }

                }


                API(requireContext()).login.login(objetoLogin).enqueue(callback)
            }
        }
    }

    private fun tratarLoginUsuario(usuario: UsuarioLogin?) {
        if (usuario != null) {

            if (usuario.resp == null) {
                Snackbar.make(
                    binding.containerLogin,
                    usuario.message,
                    Snackbar.LENGTH_LONG
                ).show();
            } else {
                setUserPreferences(requireContext(), usuario);

                val intent = Intent(activity, MainActivity::class.java);
                startActivity(intent);

            }

        } else
            Snackbar.make(
                binding.containerLogin,
                "Não foi possível fazer login",
                Snackbar.LENGTH_LONG
            ).show();
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
        fun newInstance() = LoginFragment()
    }
}