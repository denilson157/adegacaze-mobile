package com.example.adegacaze

import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doOnTextChanged
import com.example.adegacaze.databinding.FragmentUserRegisterBinding
import com.example.adegacaze.model.*
import com.example.adegacaze.service.API
import com.example.adegacaze.view.login.LoginFragment
import com.google.android.material.snackbar.Snackbar
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

private const val ARG_ID = "id"

class UserRegisterFragment : Fragment() {
    lateinit var binding: FragmentUserRegisterBinding;
    private var userId: Int? = null;

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentUserRegisterBinding.inflate(inflater, container, false)
        registrar()
        registrarSemEndereco()
        removerErros()
        carregarUsuario()


        escolherData(container?.context)


        return binding.root
    }

    private fun carregarUsuario() {
        if (userId != null && userId!! > 0) {

            val callback = object : Callback<User> {
                override fun onResponse(call: Call<User>, response: Response<User>) {
                    if (response.isSuccessful) {
                        atualizarUIUsuario(response.body())
                    } else {
                        val error = response.errorBody().toString()

                        Snackbar.make(
                            binding.scrollRegistroUsuario,
                            "Não foi possível carregar perfil selecionado",
                            Snackbar.LENGTH_LONG
                        ).show();

                        Log.e("Erro", error);
                    }
                }

                override fun onFailure(call: Call<User>, t: Throwable) {
                    Snackbar.make(
                        binding.scrollRegistroUsuario,
                        "Não foi possível se conectar com o servidor",
                        Snackbar.LENGTH_LONG
                    ).show();

                    Log.e("Erro", "Falha ao executar serviço", t);
                }

            }

            binding.buttonAddAdress.visibility = View.GONE;
            binding.buttonSignIn.text = "Salvar";

            API(requireContext()).user.pesquisarPorId(userId!!).enqueue(callback)

        }

    }

    private fun atualizarUIUsuario(usuario: User?) {
        if (usuario != null) {
            binding.editNomeCompleto.setText(usuario.name);
            binding.editEmail.setText(usuario.email);
            binding.editBirthday.setText(formatDate(usuario.birthday, "yyyy-mm-dd", "dd/mm/yyyy"));
            binding.editCelular.setText(usuario.cellphone.toString());

            binding.editPassword.visibility = View.GONE;
            binding.editPasswordInputLayout.visibility = View.GONE;
            binding.editConfirmPassword.visibility = View.GONE;
            binding.editConfirmPasswordInputLayout.visibility = View.GONE;
            binding.editIdUser.setText(usuario.id.toString());

        }
    }

    override fun onResume() {
        super.onResume()

        val tokenUsuario = getTokenUser(requireContext())

        if (tokenUsuario != null && tokenUsuario != "" && userId == null) {

            redicionarHome()

        }
    }

    private fun registrarUsuario(registrarEndereco: Boolean) {
        val objetoRegistro = UsuarioRegistro(
            formatDate(
                binding.editBirthday.text.toString(), "dd/mm/yyyy", "yyyy-mm-dd"
            ),
            binding.editPassword.text.toString(),
            "device",
            binding.editNomeCompleto.text.toString(),
            Integer.parseInt(binding.editCelular.text.toString()),
            binding.editEmail.text.toString(),
            0
        );


        val callback = object : Callback<UsuarioLogin> {
            override fun onResponse(
                call: Call<UsuarioLogin>,
                response: Response<UsuarioLogin>
            ) {
                if (response.isSuccessful) {
                    val usuario = response.body()
                    if (usuario != null) {
                        setUserPreferences(requireContext(), usuario);
                        tratarCriacaoUsuario(registrarEndereco);
                    }

                } else {
                    val error = response.errorBody().toString()

                    Snackbar.make(
                        binding.containerRegistroUsuario,
                        "Não foi possível registrar o usuário.",
                        Snackbar.LENGTH_LONG
                    ).show();

                    Log.e("Erro", error);
                }
            }

            override fun onFailure(call: Call<UsuarioLogin>, t: Throwable) {
                Snackbar.make(
                    binding.containerRegistroUsuario,
                    "Não foi possível se conectar com o servidor",
                    Snackbar.LENGTH_LONG
                ).show();

                Log.e("Erro", "Falha ao executar serviço", t);
            }

        }

        API(requireContext()).user.registrar(objetoRegistro).enqueue(callback)
    }

    private fun prepararObjetoUsuario(): User {

        return User(
            formatDate(
                binding.editBirthday.text.toString(), "dd/mm/yyyy", "yyyy-mm-dd"
            ),
            binding.editNomeCompleto.text.toString(),
            Integer.parseInt(binding.editCelular.text.toString()),
            Integer.parseInt(binding.editIdUser.text.toString()),
            false,
            binding.editEmail.text.toString()
        )
    }

    private fun tratarCriacaoUsuario(registrarEndereco: Boolean) {
        if (registrarEndereco) {

            val loginFrag = AdressUserFragment.newInstance(null);
            parentFragmentManager.beginTransaction()
                .replace(R.id.container, loginFrag)
                .addToBackStack(null)
                .commit()

        } else {

            if (userId == null || userId!! == 0)
                redicionarHome()
            else if (userId == null && userId!! == 0)

                Snackbar.make(
                    binding.scrollRegistroUsuario,
                    "Perfil atualizado",
                    Snackbar.LENGTH_LONG
                ).show();

        }
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
                    val objetoUsuario = prepararObjetoUsuario()

                    if (objetoUsuario.id == 0)
                        registrarUsuario(false);
                    else
                        atualizarUsuario(objetoUsuario)
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
        binding.buttonAddAdress.setOnClickListener() {
            if (validarCampos()) {

                if (senhasIguais()) {

                    Snackbar.make(it, "As senhas não coicidem", Snackbar.LENGTH_LONG).show()

                } else {
                    registrarUsuario(true)
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

        if (binding.editPassword.text.isNullOrEmpty() && (userId == null || userId == 0)) {
            binding.editPasswordInputLayout.error = "Informe uma senha";
            binding.editPassword.requestFocus()
            semErros = false;
        }

        if (binding.editConfirmPassword.text.isNullOrEmpty() && (userId == null || userId == 0)) {
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            userId = it.getInt(com.example.adegacaze.ARG_ID)
        }
    }


    companion object {
        @JvmStatic
        fun newInstance(id: Int?) = UserRegisterFragment()
            .apply {
                arguments = Bundle().apply {
                    if (id != null)
                        putInt(com.example.adegacaze.ARG_ID, id);
                }
            }
    }

    private fun redicionarHome() {
        val intent = Intent(activity, MainActivity::class.java)

        startActivity(intent)
    }

    private fun atualizarUsuario(user: User) {

        val callback = object : Callback<RespUser> {
            override fun onResponse(
                call: Call<RespUser>,
                response: Response<RespUser>
            ) {
                if (response.isSuccessful) {
                    Snackbar.make(
                        binding.containerRegistroUsuario,
                        "Perfil atualizado",
                        Snackbar.LENGTH_LONG
                    ).show();
                } else {
                    val error = response.errorBody().toString()

                    Snackbar.make(
                        binding.containerRegistroUsuario,
                        "Não foi possível registrar o usuário.",
                        Snackbar.LENGTH_LONG
                    ).show();

                    Log.e("Erro", error);
                }
            }

            override fun onFailure(call: Call<RespUser>, t: Throwable) {
                Snackbar.make(
                    binding.containerRegistroUsuario,
                    "Não foi possível se conectar com o servidor",
                    Snackbar.LENGTH_LONG
                ).show();

                Log.e("Erro", "Falha ao executar serviço", t);
            }

        }

        API(requireContext()).user.salvarUsuario(user.id, user)
            .enqueue(callback)

    }
}