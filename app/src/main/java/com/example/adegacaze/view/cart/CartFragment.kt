package com.example.adegacaze.view.cart

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.adegacaze.R
import com.example.adegacaze.SearchBarFragment
import com.example.adegacaze.databinding.FragmentCartBinding
import com.example.adegacaze.databinding.FragmentCartItemBinding
import com.example.adegacaze.formatarDouble
import com.example.adegacaze.model.AddCart
import com.example.adegacaze.model.AddCartResp
import com.example.adegacaze.model.Cart
import com.example.adegacaze.model.RemoveCart
import com.example.adegacaze.service.API
import com.example.adegacaze.showSnack
import com.example.adegacaze.view.MakeOrderFragment
import com.squareup.picasso.Picasso
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class CartFragment : Fragment() {
    lateinit var binding: FragmentCartBinding;

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCartBinding.inflate(inflater, container, false)
        listarCarrinho()
        fecharPedido()
        return binding.root
    }

    private fun fecharPedido() {
        binding.buttonFecharPedido.setOnClickListener {
            val orderFrag = MakeOrderFragment.newInstance();
            parentFragmentManager.beginTransaction()
                .replace(R.id.container, orderFrag)
                .addToBackStack(null)
                .commit()
        }
    }

    private fun listarCarrinho() {
        val callback = object : Callback<List<Cart>> {
            override fun onResponse(call: Call<List<Cart>>, response: Response<List<Cart>>) {
                controlarProgessBar(false)
                if (response.isSuccessful) {
                    atualizarUICarrinho(response.body())
                } else {
                    val error = response.errorBody().toString()

                    showSnack(
                        binding.containerCarrinho,
                        "Não foi possível carregar o carrinho"
                    )

                    Log.e("Erro", error);
                }
            }

            override fun onFailure(call: Call<List<Cart>>, t: Throwable) {
                showSnack(
                    binding.containerCarrinho,
                    "Não foi possível se conectar com o servidor"
                )
                controlarProgessBar(false)
                Log.e("Erro", "Falha ao executar serviço", t);
            }
        }


        API(requireContext()).cart.getCart().enqueue(callback)
        controlarProgessBar(true)
    }

    private fun atualizarUICarrinho(carrinho: List<Cart>?) {
        binding.containerCarrinho.removeAllViews()
        carrinho?.forEach {
            val carrinhoBinding = FragmentCartItemBinding.inflate(layoutInflater)

            val uriImage = Uri.parse(it.product.img_id)

            Picasso.get().load(uriImage).into(carrinhoBinding.imagemProduto);

            carrinhoBinding.textNomeProduto.text = it.product.name;
            carrinhoBinding.textTotalQuantidade.text = it.quantity.toString();
            carrinhoBinding.textValorProduto.text =
                formatarDouble(it.product.price.toDouble() * it.quantity);

            adicionarItem(carrinhoBinding, it);
            removerItem(carrinhoBinding, it);
            deletarProdutoItem(carrinhoBinding, it);
            binding.containerCarrinho.addView(carrinhoBinding.root)

        }

        if(carrinho?.count() == 0) {
            binding.frameAuxiliar.visibility = View.VISIBLE;
            binding.buttonFecharPedido.visibility = View.GONE;
        } else {
            binding.frameAuxiliar.visibility = View.GONE;
            binding.buttonFecharPedido.visibility = View.VISIBLE;
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

    companion object {
        fun newInstance() = CartFragment()
    }

    private fun adicionarItem(frag: FragmentCartItemBinding, itemCarrinho: Cart) {
        frag.imageAdd1.setOnClickListener {
            val novaQtd = Integer.parseInt(frag.textTotalQuantidade.text.toString()) + 1;
            val novoValor = itemCarrinho.product.price.toDouble() * novaQtd;

            frag.textTotalQuantidade.text = novaQtd.toString();
            frag.textValorProduto.text = novoValor.toString();
            adicionarItemCarrinho(itemCarrinho.product_id);
        }
    }

    private fun removerItem(frag: FragmentCartItemBinding, itemCarrinho: Cart) {
        frag.imageRemove1.setOnClickListener {
            var novaQtd = Integer.parseInt(frag.textTotalQuantidade.text.toString()) - 1;

            if (novaQtd < 0)
                novaQtd = 0;

            val novoValor = itemCarrinho.product.price.toDouble() * novaQtd;

            frag.textTotalQuantidade.text = novaQtd.toString();
            frag.textValorProduto.text = formatarDouble(novoValor);
            removerItemCarrinho(novaQtd == 0, itemCarrinho.product_id);

        }
    }

    private fun deletarProdutoItem(frag: FragmentCartItemBinding, itemCarrinho: Cart) {
        frag.imageRemoveItem.setOnClickListener {
            removerProdutoCarrinho(itemCarrinho.product_id)
        }
    }

    private fun adicionarItemCarrinho(produtoId: Int) {

        val callback = object : Callback<AddCartResp> {
            override fun onResponse(call: Call<AddCartResp>, response: Response<AddCartResp>) {
                if (response.isSuccessful) {


                } else {
                    val error = response.errorBody().toString()
                    showSnack(
                        binding.containerCarrinho,
                        "Não foi possível adicionar o produto ao carrinho"
                    )

                    Log.e("Erro", error);
                }
            }

            override fun onFailure(call: Call<AddCartResp>, t: Throwable) {
                showSnack(
                    binding.containerCarrinho,
                    "Não foi possível se conectar com o servidor"
                )
                Log.e("Erro", "Falha ao executar serviço", t);
            }
        }

        val objetoAdicionar = AddCart(produtoId, 1);

        API(requireContext()).cart.addCart(objetoAdicionar).enqueue(callback)

    }

    private fun removerItemCarrinho(recarregarAposRemover: Boolean, produtoId: Int) {

        val callback = object : Callback<AddCartResp> {
            override fun onResponse(call: Call<AddCartResp>, response: Response<AddCartResp>) {
                if (response.isSuccessful) {


                    if (recarregarAposRemover)
                        listarCarrinho()

                } else {
                    val error = response.errorBody().toString()

                    showSnack(
                        binding.containerCarrinho,
                        "Não foi possível remover o produto do carrinho"
                    )

                    Log.e("Erro", error);
                }
            }

            override fun onFailure(call: Call<AddCartResp>, t: Throwable) {
                showSnack(
                    binding.containerCarrinho,
                    "Não foi possível se conectar com o servidor"
                )

                Log.e("Erro", "Falha ao executar serviço", t);
            }
        }

        val objetoRemover = RemoveCart(produtoId, 1);

        API(requireContext()).cart.removeCart(objetoRemover).enqueue(callback)


    }

    private fun removerProdutoCarrinho(produtoId: Int) {

        val callback = object : Callback<AddCartResp> {
            override fun onResponse(call: Call<AddCartResp>, response: Response<AddCartResp>) {
                if (response.isSuccessful) {
                    listarCarrinho()
                } else {
                    val error = response.errorBody().toString()
                    showSnack(
                        binding.containerCarrinho,
                        "Não foi possível remover o produto do carrinho"
                    )

                    Log.e("Erro", error);
                }
            }

            override fun onFailure(call: Call<AddCartResp>, t: Throwable) {
                showSnack(
                    binding.containerCarrinho,
                    "Não foi possível se conectar com o servidor"
                )

                Log.e("Erro", "Falha ao executar serviço", t);
            }
        }

        val objetoRemover = RemoveCart(produtoId, 1);

        API(requireContext()).cart.removeProductCart(objetoRemover).enqueue(callback)


    }



    private fun controlarProgessBar(mostrar: Boolean) {
        if (mostrar)
            binding.progressBarCart.visibility = View.VISIBLE;
        else
            binding.progressBarCart.visibility = View.GONE;
    }

}