package com.example.adegacaze

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.adegacaze.databinding.FragmentHomeBinding
import com.example.adegacaze.databinding.FragmentSearchBarBinding
import com.example.adegacaze.databinding.FragmentSearchProductBinding
import com.example.adegacaze.view.HomeFragment
import com.example.adegacaze.view.ProductListFragment

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

    private fun pesquisarProdutos() {
        pesquisarProdutosPorCateogria(1, "Cervejas", R.id.frameCerveja, productName);
        pesquisarProdutosPorCateogria(2, "Vinhos", R.id.frameVinho, productName);
        pesquisarProdutosPorCateogria(3, "Destilados", R.id.frameDestilados, productName);
        pesquisarProdutosPorCateogria(4, "Não Alcoolicos", R.id.frameNaoAlcoolico, productName);

    }

    private fun pesquisarProdutosPorCateogria(
        categoriaId: Int,
        categoriaName: String,
        frame: Int,
        nomeProduto: String?
    ) {

        val produtoFragment =
            ProductListFragment.newInstance(categoriaId, categoriaName, nomeProduto)

        parentFragmentManager.beginTransaction()
            .replace(frame, produtoFragment)
            .addToBackStack(null)
            .commit()

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
}