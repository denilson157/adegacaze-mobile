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
import com.squareup.picasso.Picasso
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import com.example.adegacaze.databinding.FragmentProductBuyBinding
import com.example.adegacaze.databinding.FragmentSearchBarBinding
import com.example.adegacaze.formatarDouble
import com.example.adegacaze.model.AddCart
import com.example.adegacaze.model.AddCartResp
import com.example.adegacaze.model.Product
import com.example.adegacaze.service.API
import com.example.adegacaze.showSnack
import com.google.android.material.snackbar.Snackbar

private const val ARG_ID = "id"

class ProductBuyFragment : Fragment() {
    lateinit var binding: FragmentProductBuyBinding;
    private var productId: Int? = null;

    private var quantidade = 0;
    private var valorTotal = 0.0;
    private var precoProduto = 0.0;


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

                        showSnack(
                            binding.scrollProduto,
                            "Não foi possível carregar o produto selecionado",
                        )

                        Log.e("Erro", error);
                    }
                }

                override fun onFailure(call: Call<Product>, t: Throwable) {
                    showSnack(
                        binding.scrollProduto,
                        "Não foi possível se conectar com o servidor",
                    )

                    Log.e("Erro", "Falha ao executar serviço", t);
                }
            }

            API(requireContext()).produto.pesquisarPorId(productId!!).enqueue(callback)
            atualizarUITotal()
        }

    }

    private fun atualizarUIProduto(produto: Product?) {
        if (produto != null) {


            binding.textNome.text = produto.name;
            binding.textAntigoPreco.text = formatarDouble(produto.old_price.toDouble());
            binding.textAntigoPreco.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG;
            binding.textPreco.text = formatarDouble(produto.price.toDouble());

            precoProduto = produto.price.toDouble();
            val uriImage = Uri.parse(produto.img_id)

            Picasso.get().load(uriImage).into(binding.imagemProduto);
            controlarQuantidadeItem()
            adicionarCarrinho()
            atualizarUITotal()
        }
    }


    override fun onResume() {
        super.onResume()

        val productBinding = FragmentSearchBarBinding.inflate(layoutInflater)

        binding.fragmentSearchBar.addView(productBinding.root)

    }

    private fun adicionarCarrinho() {
        binding.buttonAddToCart.setOnClickListener {

            if (productId != null && quantidade > 0) {

                val objetoAdd = AddCart(productId!!, quantidade);

                val callback = object : Callback<AddCartResp> {

                    override fun onResponse(
                        call: Call<AddCartResp>,
                        response: Response<AddCartResp>
                    ) {
                        if (response.isSuccessful) {

                            showSnack(
                                binding.scrollProduto,
                                "Produto adicionado ao carrinho"
                            )

                        } else {
                            val error = response.errorBody().toString()
                            showSnack(
                                binding.scrollProduto,
                                "Não foi possível carregar o produto selecionado",
                            )

                            Log.e("Erro", error);
                        }
                    }

                    override fun onFailure(call: Call<AddCartResp>, t: Throwable) {

                        showSnack(
                            binding.scrollProduto,
                            "Não foi possível se conectar com o servidor",
                        )

                        Log.e("Erro", "Falha ao executar serviço", t);
                    }
                }

                API(requireContext()).cart.addCart(objetoAdd).enqueue(callback)
            }
        }
    }


    companion object {
        fun newInstance(id: Int) = ProductBuyFragment()
            .apply {
                arguments = Bundle().apply {
                    putInt(ARG_ID, id);
                }
            }
    }

    private fun controlarQuantidadeItem() {

        binding.imageAdd1.setOnClickListener {
            adicionar(1);
        }
        binding.imageAdd6.setOnClickListener {
            adicionar(6);
        }
        binding.imageAdd12.setOnClickListener {
            adicionar(12);
        }

        binding.imageRemove1.setOnClickListener {
            remover(1);
        }
        binding.imageRemove6.setOnClickListener {
            remover(6);
        }
        binding.imageRemove12.setOnClickListener {
            remover(12);
        }

    }

    private fun adicionar(qtd: Int) {
        val novaQuantidade = quantidade + qtd;
        val novoValor = novaQuantidade * precoProduto;

        quantidade = novaQuantidade;
        valorTotal = novoValor;

        atualizarUITotal();
    }

    private fun remover(qtd: Int) {
        var novaQuantidade = quantidade - qtd;
        if (novaQuantidade < 0)
            novaQuantidade = 0;

        val novoValor = novaQuantidade * precoProduto;

        quantidade = novaQuantidade;
        valorTotal = novoValor;

        atualizarUITotal();
    }

    private fun atualizarUITotal() {
        binding.textQtdTotal.text = quantidade.toString();
        binding.textValorTotal.text = formatarDouble(valorTotal)
    }
}