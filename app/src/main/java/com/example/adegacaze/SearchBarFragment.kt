package com.example.adegacaze

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.adegacaze.databinding.ActivityMainBinding
import com.example.adegacaze.databinding.FragmentListAddressBinding
import com.example.adegacaze.databinding.FragmentSearchBarBinding
import com.example.adegacaze.view.HomeFragment

class SearchBarFragment : Fragment() {

    lateinit var binding: FragmentSearchBarBinding;

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentSearchBarBinding.inflate(inflater, container, false)
        pesquisarProdutos()
        return binding.root

    }

    private fun pesquisarProdutos() {
        if (binding != null) {
            binding.fabPesquisar.setOnClickListener {

                if (binding.editPesquisar.text != null && binding.editPesquisar.text.toString() != "") {

                    val searchFrag =
                        SearchProductFragment.newInstance(binding.editPesquisar.text.toString());

                    parentFragmentManager.beginTransaction()
                        .replace(R.id.container, searchFrag)
                        .addToBackStack(null)
                        .commit()

                }
            }

        }
    }

    companion object {
        @JvmStatic
        fun newInstance() = SearchBarFragment()
    }
}