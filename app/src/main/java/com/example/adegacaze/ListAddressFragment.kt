package com.example.adegacaze

import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.adegacaze.databinding.FragmentAddressBinding
import com.example.adegacaze.databinding.FragmentListAddressBinding
import com.example.adegacaze.databinding.FragmentLoginBinding
import com.example.adegacaze.databinding.FragmentProductBinding
import com.example.adegacaze.model.Address
import com.example.adegacaze.model.Product
import com.example.adegacaze.service.IAddressService
import com.example.adegacaze.service.IProductService
import com.example.adegacaze.service.getService
import com.example.adegacaze.view.ProductBuyFragment
import com.google.android.material.snackbar.Snackbar
import com.squareup.picasso.Picasso
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
    }


    private fun listarEnderecos() {
        val service = getService().create(IAddressService::class.java)

        val call = service.listar()

        val callback = object : Callback<List<Address>> {
            override fun onResponse(call: Call<List<Address>>, response: Response<List<Address>>) {
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

                Log.e("Erro", "Falha ao executar serviço", t);
            }
        }

        call.enqueue(callback)

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


    companion object {
        @JvmStatic
        fun newInstance() = ListAddressFragment()
    }
}