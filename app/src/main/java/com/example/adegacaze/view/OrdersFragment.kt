package com.example.adegacaze.view


import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.adegacaze.*
import com.example.adegacaze.databinding.FragmentOrderBinding
import com.example.adegacaze.databinding.FragmentOrdersBinding
import com.example.adegacaze.model.Order
import com.example.adegacaze.model.ProductsItem
import com.example.adegacaze.service.API
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class OrdersFragment : Fragment() {
    lateinit var binding: FragmentOrdersBinding;

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentOrdersBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onResume() {
        super.onResume()
        listarPedidos()
        val searchFrag = SearchBarFragment.newInstance()

        parentFragmentManager.beginTransaction()
            .replace(binding.fragmentSearchBar.id, searchFrag)
            .addToBackStack(null)
            .commit()
    }

    companion object {
        fun newInstance() = OrdersFragment()
    }

    private fun listarPedidos() {
        val callback = object : Callback<List<Order>> {
            override fun onResponse(call: Call<List<Order>>, response: Response<List<Order>>) {
                controlarProgessBar(false)
                if (response.isSuccessful) {
                    atualizarUIPedidos(response.body())
                } else {
                    val error = response.errorBody().toString()

                    showSnack(
                        binding.containerPedidos,
                        "Não foi possível carregar os enderecos",
                    )

                    Log.e("Erro", error);
                }
            }

            override fun onFailure(call: Call<List<Order>>, t: Throwable) {

                showSnack(
                    binding.containerPedidos,
                    "Não foi possível se conectar com o servidor",
                )

                controlarProgessBar(false)
                Log.e("Erro", "Falha ao executar serviço", t);
            }

        }


        API(requireContext()).pedido.listar().enqueue(callback)
        controlarProgessBar(true)

    }

    private fun controlarProgessBar(mostrar: Boolean) {
        if (mostrar)
            binding.progressBarPedidos.visibility = View.VISIBLE;
        else
            binding.progressBarPedidos.visibility = View.GONE;
    }

    private fun atualizarUIPedidos(pedidos: List<Order>?) {


        if (pedidos != null) {
            binding.containerPedidos.removeAllViews()

            pedidos.forEach {
                val pedidoBinding = FragmentOrderBinding.inflate(layoutInflater)
                var produtos: List<ProductsItem> = it.products;

                pedidoBinding.textDataPedido.text = formatDate(
                    it.created_at, "yyyy-mm-dd", "dd/mm/yyyy"
                )
                pedidoBinding.textQtdItens.text = it.products.count().toString();
                pedidoBinding.textTotalPedido.text = formatarDouble(produtos.map { x ->

                    x.pivot.price * x.pivot.quantity

                }.sum())

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