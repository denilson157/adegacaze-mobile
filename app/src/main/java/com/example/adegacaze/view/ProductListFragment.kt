package com.example.adegacaze.view

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.adegacaze.R
import com.example.adegacaze.UserRegisterFragment
import com.example.adegacaze.databinding.FragmentProductListBinding
import com.example.adegacaze.databinding.FragmentProductBinding
import com.example.adegacaze.model.Product
import com.example.adegacaze.service.API
import com.example.adegacaze.service.IProductService
import com.google.android.material.snackbar.Snackbar
import com.squareup.picasso.Picasso
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProductListFragment : Fragment() {
    lateinit var binding: FragmentProductListBinding;
    lateinit var ctx: Context;

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProductListBinding.inflate(inflater, container, false)
        swipeRefresh()

        if (container != null)
            ctx = container.context;

        return binding.root
    }

    override fun onResume() {
        super.onResume()
        listarProdutos()
    }

    private fun swipeRefresh() {
        binding.swipeRefresh.setOnRefreshListener {
            listarProdutos()
        }
    }


    private fun listarProdutos() {

        mostrarShimmer(true)
        val callback = object : Callback<List<Product>> {
            override fun onResponse(call: Call<List<Product>>, response: Response<List<Product>>) {
                if (response.isSuccessful) {
                    atualizarUIProdutos(response.body())
                } else {
                    val error = response.errorBody().toString()

                    Snackbar.make(
                        binding.containerProdutos,
                        "Não foi possível carregar os produtos",
                        Snackbar.LENGTH_LONG
                    ).show();

                    Log.e("Erro", error);
                }
                mostrarShimmer(false)
            }

            override fun onFailure(call: Call<List<Product>>, t: Throwable) {
                Snackbar.make(
                    binding.containerProdutos,
                    "Não foi possível se conectar com o servidor",
                    Snackbar.LENGTH_LONG
                ).show();

                Log.e("Erro", "Falha ao executar serviço", t);
                mostrarShimmer(false)
            }
        }

        API(ctx).produto.listar().enqueue(callback)

    }

    private fun atualizarUIProdutos(produtos: List<Product>?) {


        if (produtos != null) {
            binding.containerProdutos.removeAllViews()

            produtos.forEach {
                val productBinding = FragmentProductBinding.inflate(layoutInflater)

                abrirProduto(productBinding, it.id);

                productBinding.textNome.text = it.name;
                productBinding.textAntigoPreco.text = it.oldPrice;
                productBinding.textPreco.text = it.price;
                val uriImage = Uri.parse(it.img_id)

                Picasso.get().load(uriImage).into(productBinding.imagemProduto);

                binding.containerProdutos.addView(productBinding.root)

            }
        }
    }

    private fun abrirProduto(productBinding: FragmentProductBinding, produtoId: Int) {
        productBinding.cardProduto.setOnClickListener {
            val signUpFrag = ProductBuyFragment.newInstance(produtoId);
            parentFragmentManager.beginTransaction()
                .replace(R.id.container, signUpFrag)
                .addToBackStack(null)
                .commit()
        }
    }

    private fun mostrarShimmer(mostrar: Boolean) {
        if (mostrar) {
            binding.shimmer.visibility = View.VISIBLE;
            binding.shimmer.startShimmer();
            binding.containerProdutos.visibility = View.INVISIBLE;
        } else {
            binding.shimmer.visibility = View.INVISIBLE;
            binding.shimmer.stopShimmer();
            binding.containerProdutos.visibility = View.VISIBLE;
            binding.swipeRefresh.isRefreshing = false;
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() = ProductListFragment()
    }
}