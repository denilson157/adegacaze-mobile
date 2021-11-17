package com.example.adegacaze


import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.adegacaze.databinding.FragmentUserProfileBinding


class UserProfileFragment : Fragment() {
    lateinit var binding: FragmentUserProfileBinding;


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentUserProfileBinding.inflate(inflater, container, false)
        abrirMenuEndereco()
        abrirEdicaoUsuario()
        sair()

        preencherNomeUsuario()
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        preencherNomeUsuario()
    }

    private fun preencherNomeUsuario(){
        binding.textUsername.text = getUserName(requireContext());
    }

    private fun sair() {
        binding.buttonSair.setOnClickListener {
            removeUserPreferences(requireContext());

            val intent = Intent(activity, MainActivity::class.java);

            startActivity(intent);

        }
    }

    private fun abrirMenuEndereco() {
        binding.cardEndereco.setOnClickListener {


            val addressFrag = ListAddressFragment.newInstance();
            parentFragmentManager.beginTransaction()
                .replace(R.id.container, addressFrag)
                .addToBackStack(null)
                .commit()
        }
    }

    private fun abrirEdicaoUsuario() {
        binding.cardPerfil.setOnClickListener {

            val userId = getUserId(requireContext());

            val addressFrag = UserRegisterFragment.newInstance(userId);
            parentFragmentManager.beginTransaction()
                .replace(R.id.container, addressFrag)
                .addToBackStack(null)
                .commit()
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() = UserProfileFragment()
    }
}