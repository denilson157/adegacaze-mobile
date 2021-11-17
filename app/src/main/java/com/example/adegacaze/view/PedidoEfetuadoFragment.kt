package com.example.adegacaze.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.adegacaze.R
import com.example.adegacaze.SearchBarFragment
import com.example.adegacaze.databinding.FragmentOrdersBinding
import com.example.adegacaze.databinding.FragmentPedidoEfetuadoBinding


class PedidoEfetuadoFragment : Fragment() {
    lateinit var binding: FragmentPedidoEfetuadoBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentPedidoEfetuadoBinding.inflate(inflater, container, false)
        redirecionarUsuario();
        return binding.root
    }

    private fun redirecionarUsuario() {
        binding.buttonContinuarComprando.setOnClickListener {
            val homeFrag = HomeFragment.newInstance()

            parentFragmentManager.beginTransaction()
                .replace(R.id.container, homeFrag)
                .addToBackStack(null)
                .commit()
        }
    }

    companion object {
        fun newInstance() = PedidoEfetuadoFragment()
    }

}