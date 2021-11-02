package com.example.adegacaze.view

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.squareup.picasso.Picasso
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import com.example.adegacaze.databinding.FragmentProductBuyBinding
import com.example.adegacaze.databinding.FragmentSearchBarBinding
import com.example.adegacaze.model.Product
import com.example.adegacaze.service.API
import com.google.android.material.snackbar.Snackbar

private const val ARG_ID = "id"

class ProductBuyFragment : Fragment() {
    lateinit var binding: FragmentProductBuyBinding;
    private var productId: Int? = null;
    lateinit var ctx: Context;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            productId = it.getInt(ARG_ID)
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentProductBuyBinding.inflate(inflater, container, false)
        carregarProduto()
        if (container != null)
            ctx = container.context;
        return binding.root
    }

    private fun carregarProduto() {
        if (productId != null) {
            val callback = object : Callback<Product> {

                override fun onResponse(call: Call<Product>, response: Response<Product>) {
                    if (response.isSuccessful) {
                        atualizarUIProduto(response.body())
                    } else {
                        val error = response.errorBody().toString()

                        Snackbar.make(
                            binding.scrollProduto,
                            "Não foi possível carregar o produto selecionado",
                            Snackbar.LENGTH_LONG
                        ).show();

                        Log.e("Erro", error);
                    }
                }

                override fun onFailure(call: Call<Product>, t: Throwable) {
                    Snackbar.make(
                        binding.scrollProduto,
                        "Não foi possível se conectar com o servidor",
                        Snackbar.LENGTH_LONG
                    ).show();

                    Log.e("Erro", "Falha ao executar serviço", t);
                }
            }

            API(ctx).produto.pesquisarPorId(productId!!).enqueue(callback)
        }

    }

    private fun atualizarUIProduto(produto: Product?) {
        if (produto != null) {


            binding.textNome.text = produto.name;
            binding.textAntigoPreco.text = produto.oldPrice;
            binding.textPreco.text = produto.price;
            val uriImage = Uri.parse(produto.img_id)

            Picasso.get().load(uriImage).into(binding.imagemProduto);
        }
    }


    override fun onResume() {
        super.onResume()

        val productBinding = FragmentSearchBarBinding.inflate(layoutInflater)

        binding.fragmentSearchBar.addView(productBinding.root)

    }


    companion object {
        fun newInstance(id: Int) = ProductBuyFragment()
            .apply {
                arguments = Bundle().apply {
                    putInt(ARG_ID, id);
                }
            }
    }
}