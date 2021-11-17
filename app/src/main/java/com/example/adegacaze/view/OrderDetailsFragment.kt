package com.example.adegacaze.view

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.adegacaze.SearchBarFragment
import com.example.adegacaze.databinding.FragmentOrderDetailsBinding
import com.example.adegacaze.databinding.FragmentOrderProductBinding
import com.example.adegacaze.formatDate
import com.example.adegacaze.formatarDouble
import com.example.adegacaze.model.Order
import com.example.adegacaze.model.ProductsItem
import com.example.adegacaze.service.API
import com.example.adegacaze.showSnack
import com.google.android.material.snackbar.Snackbar
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

private const val ARG_ID = "id"

class OrderDetailsFragment : Fragment() {

    lateinit var binding: FragmentOrderDetailsBinding;
    private var orderId: Int? = null;


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentOrderDetailsBinding.inflate(inflater, container, false)

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
                    controlarProgessBar(false)
                    if (response.isSuccessful) {
                        atualizarUIPedido(response.body())
                    } else {
                        val error = response.errorBody().toString()

                        showSnack(
                            binding.containerDetalhePedido,
                            "Não foi possível carregar o endereço selecionado",
                        )

                        Log.e("Erro", error);
                    }
                }

                override fun onFailure(call: Call<Order>, t: Throwable) {

                    showSnack(
                        binding.containerDetalhePedido,
                        "Não foi possível se conectar com o servidor",
                    )

                    controlarProgessBar(false)
                    Log.e("Erro", "Falha ao executar serviço", t);
                }

            }

            API(requireContext()).pedido.pesquisarPorId(orderId!!).enqueue(callback)

            controlarProgessBar(true)
        }

    }

    private fun controlarProgessBar(mostrar: Boolean) {
        if (mostrar)
            binding.progressBarPedidos.visibility = View.VISIBLE;
        else
            binding.progressBarPedidos.visibility = View.GONE;
    }

    private fun atualizarUIPedido(pedido: Order?) {
        if (pedido != null) {

            var produtos: List<ProductsItem> = pedido.products;

            binding.textDataPedido.text =
                formatDate(
                    pedido.created_at,  "yyyy-mm-dd","dd/mm/yyyy"
                )

            var total = 0.0;

            binding.frameItensPedido.removeAllViews()

            produtos.forEach {
                val produtoPedidoBinding = FragmentOrderProductBinding.inflate(layoutInflater)

                produtoPedidoBinding.textNomeProduto.text = it.name;
                produtoPedidoBinding.textPrecoProduto.text = formatarDouble(it.pivot.price);

                produtoPedidoBinding.textQuantidadeProduto.text = it.pivot.quantity.toString();
                produtoPedidoBinding.textPrecoTotal.text = formatarDouble(it.pivot.quantity * it.pivot.price)
                total += it.pivot.quantity * it.pivot.price;
                binding.frameItensPedido.addView(produtoPedidoBinding.root)

            }
            binding.textTotalPedido.text = formatarDouble(total)
        }
    }
    override fun onResume() {
        super.onResume()

        val searchFrag = SearchBarFragment.newInstance()

        parentFragmentManager.beginTransaction()
            .replace(binding.fragmentSearchBar.id, searchFrag)
            .addToBackStack(null)
            .commit()
    }



}