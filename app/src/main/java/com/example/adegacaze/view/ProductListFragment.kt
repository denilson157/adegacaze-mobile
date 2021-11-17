package com.example.adegacaze.view

import android.content.Context
import android.graphics.Paint
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
import com.example.adegacaze.formatarDouble
import com.example.adegacaze.model.Product
import com.example.adegacaze.model.ProductSearch
import com.example.adegacaze.service.API
import com.example.adegacaze.service.IProductService
import com.example.adegacaze.showSnack
import com.google.android.material.snackbar.Snackbar
import com.squareup.picasso.Picasso
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

private const val ARG_ID = "categoryId"
private const val ARG_NAME = "categoryName"
private const val ARG_PRODUCT_NAME = "productName"


class ProductListFragment : Fragment() {
    lateinit var binding: FragmentProductListBinding;
    private var categoryId: Int? = null;
    private var categoryName: String? = null;
    private var productName: String? = null;

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProductListBinding.inflate(inflater, container, false)


        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            categoryId = it.getInt(com.example.adegacaze.view.ARG_ID)
            categoryName = it.getString(com.example.adegacaze.view.ARG_NAME)
            productName = it.getString(com.example.adegacaze.view.ARG_PRODUCT_NAME)
        }
    }


    override fun onResume() {
        super.onResume()
        listarProdutos()
    }


    private fun listarProdutos() {
        binding.textProductName.text = categoryName;

        mostrarShimmer(true)
        val callback = object : Callback<List<Product>> {
            override fun onResponse(call: Call<List<Product>>, response: Response<List<Product>>) {
                if (response.isSuccessful) {
                    atualizarUIProdutos(response.body())
                } else {
                    val error = response.errorBody().toString()

                    showSnack(
                        binding.containerProdutos,
                        "Não foi possível carregar os produtos",
                    )


                    Log.e("Erro", error);
                }
                mostrarShimmer(false)
            }

            override fun onFailure(call: Call<List<Product>>, t: Throwable) {
                showSnack(
                    binding.containerProdutos,
                    "Não foi possível se conectar com o servidor",
                )

                Log.e("Erro", "Falha ao executar serviço", t);
                mostrarShimmer(false)
            }
        }

        if (categoryId != null && productName != null) {
            val objetoPesquisa = ProductSearch(categoryId!!, productName!!);
            API(requireContext()).produto.pesquisarProdutoNomePorCategoria(objetoPesquisa)
                .enqueue(callback)
        } else if (categoryId != null)
            API(requireContext()).produto.pesquisarPorCategoria(categoryId!!).enqueue(callback)

    }

    private fun atualizarUIProdutos(produtos: List<Product>?) {


        if (produtos != null) {

            if (produtos.count() == 0)
                binding.containerProduto.visibility = View.GONE;

            binding.containerProdutos.removeAllViews()

            produtos.forEach {
                val productBinding = FragmentProductBinding.inflate(layoutInflater)

                abrirProduto(productBinding, it.id);

                productBinding.textNome.text = it.name;
                productBinding.textAntigoPreco.text = formatarDouble(it.old_price.toDouble());
                productBinding.textAntigoPreco.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG;
                productBinding.textPreco.text = formatarDouble(it.price.toDouble());
                val uriImage = Uri.parse(it.img_id)

                Picasso.get().load(uriImage).into(productBinding.imagemProduto);

                binding.containerProdutos.addView(productBinding.root)

            }
        } else
            binding.containerProduto.visibility = View.GONE;
    }

    private fun abrirProduto(productBinding: FragmentProductBinding, produtoId: Int) {
        productBinding.cardProduto.setOnClickListener {
            val signUpFrag = ProductBuyFragment.newInstance(produtoId);
            parentFragmentManager?.beginTransaction()
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
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(id: Int?, name: String?, productName: String?) = ProductListFragment()
            .apply {
                arguments = Bundle().apply {
                    if (id != null)
                        putInt(com.example.adegacaze.view.ARG_ID, id);


                    if (name != null)
                        putString(com.example.adegacaze.view.ARG_NAME, name);


                    if (productName != null)
                        putString(com.example.adegacaze.view.ARG_PRODUCT_NAME, productName);

                }
            }
    }


}