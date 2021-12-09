package com.example.adegacaze.view

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.adegacaze.*
import com.example.adegacaze.databinding.FragmentOrderDetailsBinding
import com.example.adegacaze.databinding.FragmentOrderProductBinding
import com.example.adegacaze.model.AddCart
import com.example.adegacaze.model.AddCartResp
import com.example.adegacaze.model.Order
import com.example.adegacaze.model.ProductsItem
import com.example.adegacaze.service.API
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
                            "Não foi possível carregar o produto selecionado",
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
                    pedido.created_at, "yyyy-mm-dd", "dd/mm/yyyy"
                )

            var total = 0.0;

            binding.frameItensPedido.removeAllViews()

            produtos.forEach {
                val produtoPedidoBinding = FragmentOrderProductBinding.inflate(layoutInflater)

                produtoPedidoBinding.textNomeProduto.text = it.name;
                produtoPedidoBinding.textPrecoProduto.text = formatarDouble(it.pivot.price);

                produtoPedidoBinding.textQuantidadeProduto.text = it.pivot.quantity.toString();
                produtoPedidoBinding.textPrecoTotal.text =
                    formatarDouble(it.pivot.quantity * it.pivot.price)
                total += it.pivot.quantity * it.pivot.price;
                binding.frameItensPedido.addView(produtoPedidoBinding.root)

            }

            if (pedido.observation == null) {
                binding.linearlObservacoes.visibility = View.GONE;
            } else
                binding.textObservacoes.text = pedido.observation;

            binding.textoTipoPagamento.text = pedido.payment_type;
            binding.textStatusProduto.text = pedido.status.name;
            binding.textNomeEndereco.text = pedido.adress.name;
            binding.textEndereco.text = pedido.adress.street;
            binding.textNumeroCara.text = pedido.adress.number;
            binding.textComplemento.text = pedido.adress.complete;
            binding.textCidade.text = pedido.adress.cep;


            binding.textTotalPedido.text = formatarDouble(total)

            comprarNovamente(pedido)
        }
    }

    private fun comprarNovamente(pedido: Order) {
        binding.buttonPedirNovamente.setOnClickListener {

            pedido.products.forEach {
                adicionarItemCarrinho(it.id, it.pivot.quantity);
            }

            val orderFrag = MakeOrderFragment.newInstance();
            parentFragmentManager.beginTransaction()
                .replace(R.id.container, orderFrag)
                .addToBackStack(null)
                .commit()
        }
    }

    private fun adicionarItemCarrinho(produtoId: Int, quantidade: Int) {

        val callback = object : Callback<AddCartResp> {
            override fun onResponse(call: Call<AddCartResp>, response: Response<AddCartResp>) {
                if (response.isSuccessful) {


                } else {
                    val error = response.errorBody().toString()
                    showSnack(
                        binding.linearlObservacoes,
                        "Não foi possível adicionar o produto ao carrinho"
                    )

                    Log.e("Erro", error);
                }
            }

            override fun onFailure(call: Call<AddCartResp>, t: Throwable) {
                showSnack(
                    binding.linearlObservacoes,
                    "Não foi possível se conectar com o servidor"
                )
                Log.e("Erro", "Falha ao executar serviço", t);
            }
        }

        val objetoAdicionar = AddCart(produtoId, quantidade);

        API(requireContext()).cart.addCart(objetoAdicionar).enqueue(callback)

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