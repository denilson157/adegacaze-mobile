package com.example.adegacaze.view

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.adegacaze.*
import com.example.adegacaze.databinding.*
import com.example.adegacaze.model.Address
import com.example.adegacaze.model.Cart
import com.example.adegacaze.model.InsertOrder
import com.example.adegacaze.model.RespInsertOrder
import com.example.adegacaze.service.API
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class MakeOrderFragment : Fragment() {
    lateinit var binding: FragmentMakeOrderBinding;
    private var enderecoId: Int? = null;
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentMakeOrderBinding.inflate(inflater, container, false)
        listarCarrinho()
        carregarEndereco()
        confirmarPedido()
        return binding.root
    }


    private fun listarCarrinho() {
        val callback = object : Callback<List<Cart>> {
            override fun onResponse(call: Call<List<Cart>>, response: Response<List<Cart>>) {
                if (response.isSuccessful) {
                    atualizarUIItensCarrinho(response.body())
                } else {
                    val error = response.errorBody().toString()

                    showSnack(
                        binding.frameItensResumo,
                        "Não foi possível carregar o carrinho"
                    )

                    Log.e("Erro", error);
                }
            }

            override fun onFailure(call: Call<List<Cart>>, t: Throwable) {
                showSnack(
                    binding.frameItensResumo,
                    "Não foi possível se conectar com o servidor"
                )

                Log.e("Erro", "Falha ao executar serviço", t);
            }
        }


        API(requireContext()).cart.getCart().enqueue(callback)
    }


    private fun atualizarUIItensCarrinho(carrinho: List<Cart>?) {
        binding.frameItensResumo.removeAllViews()
        var totalCarrinho = 0.0;

        carrinho?.forEach {
            val carrinhoBinding = FragmentItensResumoPedidoBinding.inflate(layoutInflater)

            carrinhoBinding.textNomeProduto.text = it.product.name;
            carrinhoBinding.textQtdProduto.text = it.quantity.toString();
            carrinhoBinding.textTotalProduto.text =
                formatarDouble(it.product.price.toDouble() * it.quantity);

            totalCarrinho += it.product.price.toDouble() * it.quantity
            binding.frameItensResumo.addView(carrinhoBinding.root);
        }

        binding.textTotalPedido.text = formatarDouble(totalCarrinho);
    }

    private fun carregarEndereco() {
        val callback = object : Callback<Address> {
            override fun onResponse(call: Call<Address>, response: Response<Address>) {
                if (response.isSuccessful) {
                    carregarUIEndereco(response.body())
                } else {
                    val error = response.errorBody().toString()

                    val addressFrag = ListAddressFragment.newInstance(true);
                    parentFragmentManager.beginTransaction()
                        .replace(R.id.container, addressFrag)
                        .addToBackStack(null)
                        .commit()

                    Log.e("Erro", error);
                }
            }

            override fun onFailure(call: Call<Address>, t: Throwable) {
                showSnack(
                    binding.cardEndereco,
                    "Não foi possível se conectar com o servidor"
                )

                Log.e("Erro", "Falha ao executar serviço", t);
            }
        }


        API(requireContext()).endereco.pegarEnderecoPadrao().enqueue(callback)

    }

    private fun carregarUIEndereco(endereco: Address?) {
        if (endereco != null) {
            enderecoId = endereco.id;
            binding.textNomeEndereco.text = endereco.name;
            binding.textEndereco.text = endereco.street;
            binding.textNumeroCara.text = endereco.number;
            binding.textComplemento.text = endereco.complete;
            binding.textCidade.text = endereco.city;
            binding.textCEP.text = endereco.cep;
        } else {

            val addressFrag = ListAddressFragment.newInstance(true);
            parentFragmentManager.beginTransaction()
                .replace(R.id.container, addressFrag)
                .addToBackStack(null)
                .commit()

        }
    }

    private fun confirmarPedido() {
        binding.buttonConfirmarPedido.setOnClickListener {
            if (pedidoValidado()) {

                val textoObservacoes = binding.editObservacoes.text.toString();
                val objetoPedido = InsertOrder(
                    retornarTipoPagamento()!!,
                    textoObservacoes,
                    enderecoId!!
                )

                inserirPedido(objetoPedido)

            }

        }
    }

    private fun retornarTipoPagamento(): String? {
        var tipoPagamento: String? = null;

        if (binding.radioButtonEntrega.isChecked)
            tipoPagamento = "Pagar na entrega";
        else if (binding.radioButtonPix.isChecked)
            tipoPagamento = "PIX";

        return tipoPagamento
    }

    private fun pedidoValidado(): Boolean {
        var erro = true;

        var tipoPagamento = retornarTipoPagamento();

        if (tipoPagamento == null) {
            showSnack(
                binding.cardEndereco,
                "Preencha um tipo de pagamento"
            )
            erro = false;
        }

        if (enderecoId == null) {
            showSnack(
                binding.cardEndereco,
                "Cadastre um endereço"
            )
            erro = false;
        }

        return erro;
    }

    private fun inserirPedido(pedido: InsertOrder) {
        val callback = object : Callback<RespInsertOrder> {
            override fun onResponse(call: Call<RespInsertOrder>, response: Response<RespInsertOrder>) {
                if (response.isSuccessful) {

                    val confirmarFrag = PedidoEfetuadoFragment.newInstance();
                    parentFragmentManager.beginTransaction()
                        .replace(R.id.container, confirmarFrag)
                        .addToBackStack(null)
                        .commit()

                } else {
                    val error = response.errorBody().toString()

                    showSnack(
                        binding.cardEndereco,
                        "Não foi possível criar o pedido"
                    )

                    Log.e("Erro", error);
                }
            }

            override fun onFailure(call: Call<RespInsertOrder>, t: Throwable) {
                showSnack(
                    binding.cardEndereco,
                    "Não foi possível se conectar com o servidor"
                )

                Log.e("Erro", "Falha ao executar serviço", t);
            }
        }


        API(requireContext()).pedido.inserir(pedido).enqueue(callback)
    }

    companion object {
        @JvmStatic
        fun newInstance() = MakeOrderFragment()
    }

    override fun onResume() {
        super.onResume()
        tratarBottom()
    }

    private fun tratarBottom() {

        /*val view = view;
        val bottom = view?.findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        bottom?.visibility = View.GONE;*/

    }
}