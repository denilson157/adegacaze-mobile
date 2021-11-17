package com.example.adegacaze

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doOnTextChanged
import com.example.adegacaze.databinding.FragmentAdressUserBinding
import com.example.adegacaze.model.Address
import com.example.adegacaze.model.CEP
import com.example.adegacaze.model.RespAddress
import com.example.adegacaze.service.API
import com.example.adegacaze.service.APICep
import com.example.adegacaze.view.HomeFragment
import com.google.android.material.snackbar.Snackbar
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

private const val ARG_ID = "id"

class AdressUserFragment : Fragment() {
    lateinit var binding: FragmentAdressUserBinding;
    private var addressId: Int? = null;

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAdressUserBinding.inflate(inflater, container, false)
        carregarEndereco()
        validarEnderecoParaSalvar()
        removerErros()
        buscarCEP()
        return binding.root

    }

    private fun buscarCEP() {
        binding.fabPesquisaCEP.setOnClickListener {
            if (binding.editCep.text != null) {

                val callback = object : Callback<CEP> {
                    override fun onResponse(call: Call<CEP>, response: Response<CEP>) {
                        if (response.isSuccessful) {
                            atualizarUICEP(response.body())
                        } else {
                            val error = response.errorBody().toString()

                            showSnack(
                                binding.scrollEndereco,
                                "Não foi possível encontrar o cep digitado",
                            )

                            Log.e("Erro", error);
                        }
                    }

                    override fun onFailure(call: Call<CEP>, t: Throwable) {
                        showSnack(
                            binding.scrollEndereco,
                            "Não foi possível se conectar com o servidor",
                        )

                        Log.e("Erro", "Falha ao executar serviço", t);
                    }

                }


                APICep(requireContext()).cep.buscarCep(binding.editCep.text.toString())
                    .enqueue(callback)

            }
        }
    }

    private fun atualizarUICEP(cep: CEP?) {
        if (cep != null) {
            binding.editLogradouro.setText(cep.logradouro)
            binding.editEstado.setText(cep.uf)
            binding.editCidade.setText(cep.localidade)

        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            addressId = it.getInt(com.example.adegacaze.ARG_ID)
        }
    }


    companion object {
        @JvmStatic
        fun newInstance(id: Int?) = AdressUserFragment()
            .apply {
                arguments = Bundle().apply {
                    if (id != null)
                        putInt(com.example.adegacaze.ARG_ID, id);
                }
            }
    }


    private fun carregarEndereco() {
        if (addressId != null && addressId!! > 0) {

            val callback = object : Callback<Address> {
                override fun onResponse(call: Call<Address>, response: Response<Address>) {
                    if (response.isSuccessful) {
                        atualizarUIEndereco(response.body())
                    } else {
                        val error = response.errorBody().toString()

                        showSnack(
                            binding.scrollEndereco,
                            "Não foi possível carregar o endereço selecionado",
                        )

                        Log.e("Erro", error);
                    }
                }

                override fun onFailure(call: Call<Address>, t: Throwable) {
                    showSnack(
                        binding.scrollEndereco,
                        "Não foi possível se conectar com o servidor",
                    )

                    Log.e("Erro", "Falha ao executar serviço", t);
                }

            }


            API(requireContext()).endereco.pesquisarPorId(addressId!!).enqueue(callback)

        }

    }

    private fun atualizarUIEndereco(endereco: Address?) {
        if (endereco != null) {

            binding.editAdressName.setText(endereco.name);
            binding.editCep.setText(endereco.cep);
            binding.editCidade.setText(endereco.city);
            binding.editComplemento.setText(endereco.complete);
            binding.editEstado.setText(endereco.state);
            binding.editLogradouro.setText(endereco.street);
            binding.editNumero.setText(endereco.number);
            binding.editIdAddress.setText(endereco.id.toString());
            binding.switchEnderecoPadrao.isChecked = endereco.pattern == 1;

        }
    }

    private fun validarEnderecoParaSalvar() {
        binding.buttonSalvarEndereco.setOnClickListener {
            if (validarCampos()) {
                salvarEndereco()
            }
        }
    }

    private fun salvarEndereco() {


        val endereco = prepararObjetoEndereco()

        val callback = object : Callback<RespAddress> {

            override fun onResponse(call: Call<RespAddress>, response: Response<RespAddress>) {
                if (response.isSuccessful) {

                    redirecionarUsuarioAposSalvar()

                } else {
                    val error = response.errorBody().toString()

                    showSnack(
                        binding.scrollEndereco,
                        "Não foi possível salvar o endereço",
                    )

                    Log.e("Erro", error);
                }
            }

            override fun onFailure(call: Call<RespAddress>, t: Throwable) {

                showSnack(
                    binding.scrollEndereco,
                    "Não foi possível se conectar com o servidor",
                )

                Log.e("Erro", "Falha ao executar serviço", t);
            }
        }


        if (endereco.id == 0)
            API(requireContext()).endereco.criarEndereco(endereco).enqueue(callback)
        else
            API(requireContext()).endereco.salvarEndereco(endereco.id, endereco)
                .enqueue(callback)

    }

    private fun prepararObjetoEndereco(): Address {

        var check = 1;
        if (!binding.switchEnderecoPadrao.isChecked)
            check = 0;

        return Address(
            binding.editNumero.text.toString(),
            binding.editCidade.text.toString(),
            binding.editLogradouro.text.toString(),
            "A",
            binding.editAdressName.text.toString(),
            binding.editEstado.text.toString(),
            Integer.parseInt(binding.editIdAddress.text.toString()),
            binding.editCep.text.toString(),
            binding.editComplemento.text.toString(),
            check
        )

    }

    private fun validarCampos(): Boolean {
        var semErros = true;

        if (binding.editAdressName.text.isNullOrEmpty()) {
            binding.textAdressName.error = "Informe um nome";
            binding.editAdressName.requestFocus()
            semErros = false;
        }

        if (binding.editCep.text.isNullOrEmpty()) {
            binding.textCep.error = "Informe o CEP";
            binding.editCep.requestFocus()
            semErros = false;
        }

        if (binding.editCidade.text.isNullOrEmpty()) {
            binding.textCidade.error = "Informe a cidade";
            binding.editCidade.requestFocus()
            semErros = false;
        }

        if (binding.editComplemento.text.isNullOrEmpty()) {
            binding.textComplemento.error = "Informe o complemento";
            binding.editComplemento.requestFocus()
            semErros = false;
        }

        if (binding.editEstado.text.isNullOrEmpty()) {
            binding.textEstado.error = "Informe o estado";
            binding.editEstado.requestFocus()
            semErros = false;
        }

        if (binding.editLogradouro.text.isNullOrEmpty()) {
            binding.textLogradouro.error = "Informe o logradouro";
            binding.editLogradouro.requestFocus()
            semErros = false;
        }


        if (binding.editNumero.text.isNullOrEmpty()) {
            binding.textNumero.error = "Informe o número";
            binding.editNumero.requestFocus()
            semErros = false;
        }

        return semErros;
    }

    private fun removerErros() {

        binding.editAdressName.doOnTextChanged() { _, _, _, _ ->
            binding.textAdressName.isErrorEnabled = false
        }
        binding.editCep.doOnTextChanged() { _, _, _, _ ->
            binding.textCep.isErrorEnabled = false
        }

        binding.editCidade.doOnTextChanged() { _, _, _, _ ->
            binding.textCidade.isErrorEnabled = false
        }
        binding.editComplemento.doOnTextChanged() { _, _, _, _ ->
            binding.textComplemento.isErrorEnabled = false
        }

        binding.editEstado.doOnTextChanged() { _, _, _, _ ->
            binding.textEstado.isErrorEnabled = false
        }

        binding.editLogradouro.doOnTextChanged() { _, _, _, _ ->
            binding.textLogradouro.isErrorEnabled = false
        }

        binding.editNumero.doOnTextChanged() { _, _, _, _ ->
            binding.textNumero.isErrorEnabled = false
        }
    }

    private fun redirecionarUsuarioAposSalvar() {

        val addressFrag = HomeFragment.newInstance();
        parentFragmentManager.beginTransaction()
            .replace(R.id.container, addressFrag)
            .addToBackStack(null)
            .commit()

        showSnack(
            binding.scrollEndereco,
            "Endereço salvo",
        )
    }

}