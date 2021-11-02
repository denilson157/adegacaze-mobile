package com.example.adegacaze

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.adegacaze.databinding.FragmentAdressUserBinding
import com.example.adegacaze.model.Address
import com.example.adegacaze.service.API
import com.google.android.material.snackbar.Snackbar
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

private const val ARG_ID = "id"

class AdressUserFragment : Fragment() {
    lateinit var binding: FragmentAdressUserBinding;
    private var addressId: Int? = null;
    lateinit var ctx: Context;

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAdressUserBinding.inflate(inflater, container, false)
        carregarEndereco()
        if (container != null)
            ctx = container.context;
        return binding.root

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
                    if(id != null)
                        putInt(com.example.adegacaze.ARG_ID, id);
                }
            }
    }


    private fun carregarEndereco() {
        if (addressId != null) {

            val callback = object : Callback<Address> {
                override fun onResponse(call: Call<Address>, response: Response<Address>) {
                    if (response.isSuccessful) {
                        atualizarUIEndereco(response.body())
                    } else {
                        val error = response.errorBody().toString()

                        Snackbar.make(
                            binding.scrollEndereco,
                            "Não foi possível carregar o endereço selecionado",
                            Snackbar.LENGTH_LONG
                        ).show();

                        Log.e("Erro", error);
                    }
                }

                override fun onFailure(call: Call<Address>, t: Throwable) {
                    Snackbar.make(
                        binding.scrollEndereco,
                        "Não foi possível se conectar com o servidor",
                        Snackbar.LENGTH_LONG
                    ).show();

                    Log.e("Erro", "Falha ao executar serviço", t);
                }

            }


            API(ctx).endereco.pesquisarPorId(addressId!!).enqueue(callback)

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



        }
    }


}