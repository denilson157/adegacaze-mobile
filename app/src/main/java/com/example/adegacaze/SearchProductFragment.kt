package com.example.adegacaze

import android.graphics.Paint
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.adegacaze.databinding.*
import com.example.adegacaze.model.Category
import com.example.adegacaze.model.Product
import com.example.adegacaze.model.ProductSearch
import com.example.adegacaze.service.API
import com.example.adegacaze.view.HomeFragment
import com.example.adegacaze.view.ProductBuyFragment
import com.example.adegacaze.view.ProductListFragment
import com.squareup.picasso.Picasso
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

private const val ARG_NAME = "name"

class SearchProductFragment : Fragment() {
    lateinit var binding: FragmentSearchProductBinding;
    private var productName: String? = null;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            productName = it.getString(com.example.adegacaze.ARG_NAME)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentSearchProductBinding.inflate(inflater, container, false)
        pesquisarProdutos()
        swipeRefresh()

        return binding.root
    }


    private fun swipeRefresh() {
        pesquisarProdutos()
    }

    companion object {
        @JvmStatic
        fun newInstance(productName: String?) = SearchProductFragment()
            .apply {
                arguments = Bundle().apply {
                    if (productName != null)
                        putString(com.example.adegacaze.ARG_NAME, productName);
                }
            }
    }


    override fun onResume() {
        super.onResume()

        val produtoFragment = SearchBarFragment.newInstance()

        parentFragmentManager.beginTransaction()
            .replace(binding.fragmentSearchBar.id, produtoFragment)
            .addToBackStack(null)
            .commit()

    }


    private fun pesquisarProdutos() {


        val callbackCategorias = object : Callback<List<Category>> {
            override fun onResponse(
                call: Call<List<Category>>,
                response: Response<List<Category>>
            ) {
                if (response.isSuccessful) {

                    val categorias = response.body()

                    binding.layoutProdutos.removeAllViews()
                    categorias?.forEach {
                        pesquisarProdutosPorCateogria(it)
                    }

                } else {
                    val error = response.errorBody().toString()

                    showSnack(
                        binding.layoutProdutos,
                        "Não foi possível carregar as categorias dos produtos",
                    )

                    Log.e("Erro", error);
                }
            }

            override fun onFailure(call: Call<List<Category>>, t: Throwable) {
                showSnack(
                    binding.layoutProdutos,
                    "Não foi possível se conectar com o servidor",
                )

                Log.e("Erro", "Falha ao executar serviço", t);
            }
        }

        API(requireContext()).category.listar().enqueue(callbackCategorias)
    }


    private fun pesquisarProdutosPorCateogria(
        categoria: Category
    ) {
        val fragProductList = FragmentProductListBinding.inflate(layoutInflater)

        fragProductList.textProductName.text = categoria.name;
        binding.layoutProdutos.addView(fragProductList.root)
        mostrarShimmerProduto(true, fragProductList)

        val callback = object : Callback<List<Product>> {
            override fun onResponse(call: Call<List<Product>>, response: Response<List<Product>>) {
                if (response.isSuccessful) {
                    atualizarUIProdutos(response.body(), fragProductList)
                } else {
                    val error = response.errorBody().toString()

                    showSnack(
                        fragProductList.containerProdutos,
                        "Não foi possível carregar os produtos",
                    )

                    Log.e("Erro", error);
                }
                mostrarShimmerProduto(false, fragProductList)
            }

            override fun onFailure(call: Call<List<Product>>, t: Throwable) {
                showSnack(
                    fragProductList.containerProdutos,
                    "Não foi possível se conectar com o servidor",
                )

                Log.e("Erro", "Falha ao executar serviço", t);
                mostrarShimmerProduto(false, fragProductList)
            }
        }

        val objeto = ProductSearch(categoria.id, productName!!)

        API(requireContext()).produto.pesquisarProdutoNomePorCategoria(objeto).enqueue(callback)
    }

    private fun mostrarShimmerProduto(mostrar: Boolean, frag: FragmentProductListBinding) {
        if (mostrar) {
            frag.shimmer.visibility = View.VISIBLE;
            frag.shimmer.startShimmer();
            frag.containerProdutos.visibility = View.INVISIBLE;
        } else {
            frag.shimmer.visibility = View.INVISIBLE;
            frag.shimmer.stopShimmer();
            frag.containerProdutos.visibility = View.VISIBLE;
        }
    }

    private fun atualizarUIProdutos(produtos: List<Product>?, frag: FragmentProductListBinding) {


        if (produtos != null) {

            if (produtos.count() == 0)
                frag.containerProduto.visibility = View.GONE;

            frag.containerProdutos.removeAllViews()

            produtos.forEach {
                val productBinding = FragmentProductBinding.inflate(layoutInflater)

                abrirProduto(productBinding, it.id);

                productBinding.textNome.text = it.name;

                if (it.promotion == "1") {
                    productBinding.textAntigoPreco.text = formatarDouble(it.old_price.toDouble());
                    productBinding.textAntigoPreco.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG;
                } else
                    productBinding.textAntigoPreco.visibility = View.GONE;

                productBinding.textPreco.text = formatarDouble(it.price.toDouble());
                val uriImage = Uri.parse(it.img_id)

                Picasso.get().load(uriImage).into(productBinding.imagemProduto);

                frag.containerProdutos.addView(productBinding.root)

            }
        } else
            frag.containerProduto.visibility = View.GONE;
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


}