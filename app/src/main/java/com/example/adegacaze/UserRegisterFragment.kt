package com.example.adegacaze

import android.app.DatePickerDialog
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doOnTextChanged
import com.example.adegacaze.databinding.FragmentUserRegisterBinding
import com.google.android.material.snackbar.Snackbar
import java.util.*

class UserRegisterFragment : Fragment() {
    lateinit var binding: FragmentUserRegisterBinding;


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentUserRegisterBinding.inflate(inflater, container, false)
        registrar()
        registrarSemEndereco()
        removerErros()
        escolherData(container?.getContext())
        return binding.root
    }


    private fun escolherData(context: Context?) {
        val calendar = Calendar.getInstance()
        val yyyy = calendar.get(Calendar.YEAR)
        val mm = calendar.get(Calendar.MONTH)
        val dd = calendar.get(Calendar.DAY_OF_MONTH)

        binding.imageCalendar.setOnClickListener {
            if (context != null)
                DatePickerDialog(context, { calView, sYear, sMonth, sDay ->
                    binding.editBirthday.setText("${sDay}/${sMonth}/${sYear}")
                }, yyyy, mm, dd).show()
        }
    }

    private fun registrar() {
        binding.buttonSignIn.setOnClickListener() {
            if (validarCampos()) {

                if (senhasIguais()) {

                    Snackbar.make(it, "As senhas não coicidem", Snackbar.LENGTH_LONG).show()

                } else {

                    val email = binding.editEmail.text;
                    val senha = binding.editPassword.text;

                }

            }
        }
    }

    private fun senhasIguais(): Boolean {
        if (binding.editPassword.text != binding.editConfirmPassword.text)
            return false
        return true
    }


    private fun registrarSemEndereco() {
        binding.buttonSignIn.setOnClickListener() {
            if (validarCampos()) {

                if (senhasIguais()) {

                    Snackbar.make(it, "", Snackbar.LENGTH_LONG).show()

                } else {

                    val email = binding.editEmail.text;
                    val senha = binding.editPassword.text;

                }

            }
        }
    }

    private fun validarCampos(): Boolean {
        var semErros = true;

        if (binding.editNomeCompleto.text.isNullOrEmpty()) {
            binding.editNomeCompletoInputLayout.error = "Informe um nome";
            binding.editNomeCompleto.requestFocus()
            semErros = false;
        }

        if (binding.editEmail.text.isNullOrEmpty()) {
            binding.editEmailInputLayout.error = "Informe uma email";
            binding.editEmail.requestFocus()
            semErros = false;
        }

        if (binding.editBirthday.text.isNullOrEmpty()) {
            binding.editBirthdayInputLayout.error = "Informe uma data";
            binding.editBirthday.requestFocus()
            semErros = false;
        }

        if (binding.editCelular.text.isNullOrEmpty()) {
            binding.editCelularInputLayout.error = "Informe um celular";
            binding.editCelular.requestFocus()
            semErros = false;
        }

        if (binding.editPassword.text.isNullOrEmpty()) {
            binding.editPasswordInputLayout.error = "Informe uma senha";
            binding.editPassword.requestFocus()
            semErros = false;
        }

        if (binding.editConfirmPassword.text.isNullOrEmpty()) {
            binding.editConfirmPasswordInputLayout.error = "Informe uma senha";
            binding.editConfirmPassword.requestFocus()
            semErros = false;
        }


        return semErros;
    }

    private fun removerErros() {
        binding.editNomeCompleto.doOnTextChanged() { _, _, _, _ ->
            binding.editNomeCompletoInputLayout.isErrorEnabled = false
        }
        binding.editEmail.doOnTextChanged() { _, _, _, _ ->
            binding.editEmailInputLayout.isErrorEnabled = false
        }

        binding.editBirthday.doOnTextChanged() { _, _, _, _ ->
            binding.editBirthdayInputLayout.isErrorEnabled = false
        }
        binding.editCelular.doOnTextChanged() { _, _, _, _ ->
            binding.editCelularInputLayout.isErrorEnabled = false
        }

        binding.editPassword.doOnTextChanged() { _, _, _, _ ->
            binding.editPasswordInputLayout.isErrorEnabled = false
        }

        binding.editConfirmPassword.doOnTextChanged() { _, _, _, _ ->
            binding.editConfirmPasswordInputLayout.isErrorEnabled = false
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() = UserRegisterFragment()
    }
}