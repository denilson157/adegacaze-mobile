package com.example.adegacaze.view

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.adegacaze.R
import com.example.adegacaze.databinding.FragmentOrderBinding
import com.example.adegacaze.databinding.FragmentOrdersBinding
import com.example.adegacaze.model.Order
import com.example.adegacaze.model.ProductsItem
import com.example.adegacaze.service.API
import com.google.android.material.snackbar.Snackbar
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class OrdersFragment : Fragment() {
    lateinit var binding: FragmentOrdersBinding;
    lateinit var ctx: Context;

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentOrdersBinding.inflate(inflater, container, false)

        if (container != null)
            ctx = container.context;

        return binding.root
    }

    override fun onResume() {
        super.onResume()
        listarPedidos()
    }

    companion object {
        fun newInstance() = OrdersFragment()
    }

    private fun listarPedidos() {
        val callback = object : Callback<List<Order>> {
            override fun onResponse(call: Call<List<Order>>, response: Response<List<Order>>) {
                if (response.isSuccessful) {
                    atualizarUIPedidos(response.body())
                } else {
                    val error = response.errorBody().toString()

                    Snackbar.make(
                        binding.containerPedidos,
                        "Não foi possível carregar os enderecos",
                        Snackbar.LENGTH_LONG
                    ).show();

                    Log.e("Erro", error);
                }
            }

            override fun onFailure(call: Call<List<Order>>, t: Throwable) {
                Snackbar.make(
                    binding.containerPedidos,
                    "Não foi possível se conectar com o servidor",
                    Snackbar.LENGTH_LONG
                ).show();

                Log.e("Erro", "Falha ao executar serviço", t);
            }

        }


        API(ctx).pedido.listar().enqueue(callback)

    }

    private fun atualizarUIPedidos(pedidos: List<Order>?) {


        if (pedidos != null) {
            binding.containerPedidos.removeAllViews()

            pedidos.forEach {
                val pedidoBinding = FragmentOrderBinding.inflate(layoutInflater)
                var produtos: List<ProductsItem> = it.products;

                pedidoBinding.textDataPedido.text = it.createdAt;
                pedidoBinding.textQtdItens.text = it.products.count().toString();
                pedidoBinding.textTotalPedido.text = produtos.map { x -> x.price }.sum().toString()

                abrirPedido(pedidoBinding, it.id)


                binding.containerPedidos.addView(pedidoBinding.root)

            }
        }
    }

    private fun abrirPedido(addressBinding: FragmentOrderBinding, orderId: Int) {
        addressBinding.cardPedido.setOnClickListener {
            val signUpFrag = OrderDetailsFragment.newInstance(orderId);
            parentFragmentManager.beginTransaction()
                .replace(R.id.container, signUpFrag)
                .addToBackStack(null)
                .commit()
        }
    }
}