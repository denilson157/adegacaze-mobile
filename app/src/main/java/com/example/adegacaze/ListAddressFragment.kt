package com.example.adegacaze

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.adegacaze.databinding.FragmentAddressBinding
import com.example.adegacaze.databinding.FragmentListAddressBinding
import com.example.adegacaze.model.Address
import com.example.adegacaze.service.API
import com.google.android.material.snackbar.Snackbar
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class ListAddressFragment : Fragment() {
    lateinit var binding: FragmentListAddressBinding;


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentListAddressBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onResume() {
        super.onResume()
        listarEnderecos()
        novoEndereco()
    }


    private fun listarEnderecos() {
        val callback = object : Callback<List<Address>> {
            override fun onResponse(call: Call<List<Address>>, response: Response<List<Address>>) {
                controlarProgessBar(false)
                if (response.isSuccessful) {
                    atualizarUIEnderecos(response.body())
                } else {
                    val error = response.errorBody().toString()

                    Snackbar.make(
                        binding.containerEnderecos,
                        "Não foi possível carregar os enderecos",
                        Snackbar.LENGTH_LONG
                    ).show();
                    Log.e("Erro", error);
                }

            }

            override fun onFailure(call: Call<List<Address>>, t: Throwable) {
                Snackbar.make(
                    binding.containerEnderecos,
                    "Não foi possível se conectar com o servidor",
                    Snackbar.LENGTH_LONG
                ).show();
                controlarProgessBar(false)
                Log.e("Erro", "Falha ao executar serviço", t);
            }
        }


        API(requireContext()).endereco.listar().enqueue(callback)

        controlarProgessBar(true)
    }

    private fun controlarProgessBar(mostrar: Boolean) {
        if (mostrar)
            binding.progressBar.visibility = View.VISIBLE;
        else
            binding.progressBar.visibility = View.GONE;
    }

    private fun atualizarUIEnderecos(enderecos: List<Address>?) {


        if (enderecos != null) {
            binding.containerEnderecos.removeAllViews()

            enderecos.forEach {
                val enderecoBinding = FragmentAddressBinding.inflate(layoutInflater)

                abrirEndereco(enderecoBinding, it.id);

                enderecoBinding.textNomeEndereco.text = it.name;
                enderecoBinding.textRua.text = it.street;
                enderecoBinding.textNumeroEndereco.text = it.number;
                if (it.complete != null)
                    enderecoBinding.textComplementoEndereco.text = it.complete;
                else
                    enderecoBinding.textComplementoEndereco.text = "";
                enderecoBinding.textCidadeEndereco.text = it.city;
                enderecoBinding.textCepEndereco.text = it.cep;

                binding.containerEnderecos.addView(enderecoBinding.root)

            }
        }
    }

    private fun abrirEndereco(addressBinding: FragmentAddressBinding, addressId: Int) {
        addressBinding.cardEndereco.setOnClickListener {
            val signUpFrag = AdressUserFragment.newInstance(addressId);
            parentFragmentManager.beginTransaction()
                .replace(R.id.container, signUpFrag)
                .addToBackStack(null)
                .commit()
        }
    }

    private fun novoEndereco() {
        binding.buttonNovoEndereco.setOnClickListener {
            val signUpFrag = AdressUserFragment.newInstance(null);
            parentFragmentManager.beginTransaction()
                .replace(R.id.container, signUpFrag)
                .addToBackStack(null)
                .commit()
        }
    }


    companion object {
        @JvmStatic
        fun newInstance() = ListAddressFragment()
    }
}