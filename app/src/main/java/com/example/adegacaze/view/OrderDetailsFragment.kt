package com.example.adegacaze.view

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.adegacaze.databinding.FragmentOrderDetailsBinding
import com.example.adegacaze.databinding.FragmentOrderProductBinding
import com.example.adegacaze.model.Order
import com.example.adegacaze.model.ProductsItem
import com.example.adegacaze.service.API
import com.google.android.material.snackbar.Snackbar
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

private const val ARG_ID = "id"

class OrderDetailsFragment : Fragment() {

    lateinit var binding: FragmentOrderDetailsBinding;
    private var orderId: Int? = null;
    lateinit var ctx: Context;


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentOrderDetailsBinding.inflate(inflater, container, false)
        if (container != null)
            ctx = container.context;
        carregarOrder()
        return binding.root

    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            orderId = it.getInt(com.example.adegacaze.view.ARG_ID)
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(id: Int?) = OrderDetailsFragment()
            .apply {
                arguments = Bundle().apply {
                    if (id != null)
                        putInt(com.example.adegacaze.view.ARG_ID, id);
                }
            }
    }


    private fun carregarOrder() {
        if (orderId != null) {


            val callback = object : Callback<Order> {
                override fun onResponse(call: Call<Order>, response: Response<Order>) {
                    if (response.isSuccessful) {
                        atualizarUIPedido(response.body())
                    } else {
                        val error = response.errorBody().toString()

                        Snackbar.make(
                            binding.containerDetalhePedido,
                            "Não foi possível carregar o endereço selecionado",
                            Snackbar.LENGTH_LONG
                        ).show();

                        Log.e("Erro", error);
                    }
                }

                override fun onFailure(call: Call<Order>, t: Throwable) {
                    Snackbar.make(
                        binding.containerDetalhePedido,
                        "Não foi possível se conectar com o servidor",
                        Snackbar.LENGTH_LONG
                    ).show();

                    Log.e("Erro", "Falha ao executar serviço", t);
                }

            }

            API(ctx).pedido.pesquisarPorId(orderId!!).enqueue(callback)

        }

    }

    private fun atualizarUIPedido(pedido: Order?) {
        if (pedido != null) {

            var produtos: List<ProductsItem> = pedido.products;

            binding.textDataPedido.text = pedido.createdAt;
            binding.textTotalPedido.text = produtos.map { x -> x.price }.sum().toString()

            binding.frameItensPedido.removeAllViews()

            produtos.forEach {
                val produtoPedidoBinding = FragmentOrderProductBinding.inflate(layoutInflater)

                produtoPedidoBinding.textNomeProduto.text = it.name;
                produtoPedidoBinding.textPrecoProduto.text = it.pivot.price.toString();

                produtoPedidoBinding.textQuantidadeProduto.text = it.pivot.quantity.toString();
                produtoPedidoBinding.textPrecoTotal.text =
                    (it.pivot.quantity * it.pivot.price).toString();

                binding.frameItensPedido.addView(produtoPedidoBinding.root)

            }
        }
    }


}