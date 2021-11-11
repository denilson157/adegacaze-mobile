package com.example.adegacaze.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.adegacaze.R
import com.example.adegacaze.databinding.FragmentHomeBinding
import com.example.adegacaze.databinding.FragmentSearchBarBinding

private const val ARG_NAME = "name"

class HomeFragment : Fragment() {
    lateinit var binding: FragmentHomeBinding;
    private var productName: String? = null;

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentHomeBinding.inflate(inflater, container, false)
        pesquisarProdutos()
        swipeRefresh()

        return binding.root
    }

    private fun pesquisarProdutos() {
        pesquisarProdutosPorCateogria(1, "Cervejas", R.id.frameCerveja, productName);
        pesquisarProdutosPorCateogria(2, "Vinhos", R.id.frameVinho, productName);
        pesquisarProdutosPorCateogria(3, "Destilados", R.id.frameDestilados, productName);
        pesquisarProdutosPorCateogria(4, "Não Alcoolicos", R.id.frameNaoAlcoolico, productName);

    }

    private fun swipeRefresh() {

    }

    private fun pesquisarProdutosPorCateogria(categoriaId: Int, categoriaName: String, frame: Int, nomeProduto: String?) {

        val produtoFragment = ProductListFragment.newInstance(categoriaId, categoriaName, nomeProduto)

        parentFragmentManager.beginTransaction()
            .replace(frame, produtoFragment)
            .addToBackStack(null)
            .commit()

    }

    override fun onResume() {
        super.onResume()

        val productBinding = FragmentSearchBarBinding.inflate(layoutInflater)

        binding.fragmentSearchBar.addView(productBinding.root)

    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            productName = it.getString(com.example.adegacaze.view.ARG_NAME)
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(productName: String?) = HomeFragment()
            .apply {
                arguments = Bundle().apply {
                    if (productName != null)
                        putString(com.example.adegacaze.view.ARG_NAME, productName);
                }
            }
    }
}