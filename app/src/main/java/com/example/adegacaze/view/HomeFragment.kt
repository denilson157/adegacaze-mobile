package com.example.adegacaze.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.adegacaze.R
import com.example.adegacaze.SearchBarFragment
import com.example.adegacaze.databinding.FragmentHomeBinding
import com.example.adegacaze.databinding.FragmentSearchBarBinding


class HomeFragment : Fragment() {
    lateinit var binding: FragmentHomeBinding;

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
        pesquisarProdutosPorCateogria(1, "Cervejas", R.id.frameCerveja, null);
        pesquisarProdutosPorCateogria(2, "Vinhos", R.id.frameVinho, null);
        pesquisarProdutosPorCateogria(3, "Destilados", R.id.frameDestilados, null);
        pesquisarProdutosPorCateogria(4, "Não Alcoolicos", R.id.frameNaoAlcoolico, null);

    }

    private fun swipeRefresh() {
        pesquisarProdutos()
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

    override fun onResume() {
        super.onResume()

        val searchFrag = SearchBarFragment.newInstance()

        parentFragmentManager.beginTransaction()
            .replace(binding.fragmentSearchBar.id, searchFrag)
            .addToBackStack(null)
            .commit()
    }


    companion object {
        @JvmStatic
        fun newInstance() = HomeFragment()
    }
}