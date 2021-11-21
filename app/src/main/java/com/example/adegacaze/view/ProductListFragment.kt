package com.example.adegacaze.view


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.adegacaze.R
import com.example.adegacaze.databinding.FragmentProductListBinding




class ProductListFragment : Fragment() {
    lateinit var binding: FragmentProductListBinding;

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_product_list, container, false)
    }

    companion object {
        @JvmStatic
        fun newInstance() = ProductListFragment()
    }
}