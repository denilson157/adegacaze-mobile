package com.example.adegacaze

import android.app.Activity.RESULT_CANCELED
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.adegacaze.databinding.FragmentSearchBarBinding
import com.example.adegacaze.view.ProductBuyFragment
import com.example.adegacaze.view.QrCodeActivity

class SearchBarFragment : Fragment() {

    lateinit var binding: FragmentSearchBarBinding;

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentSearchBarBinding.inflate(inflater, container, false)

        pesquisarProdutos()
        iniciarQrCode();


        return binding.root

    }

    private fun iniciarQrCode() {
        binding.qrCodeImage.setOnClickListener {
            val qrCodeIntent = Intent(activity, QrCodeActivity::class.java);
            startActivityForResult(qrCodeIntent, 1);

        }
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)


        if (requestCode == 1 && data != null) {
            if (resultCode == RESULT_OK) {

                val produtoId = Integer.parseInt(data.getStringExtra("qrcode") as String);

                val productFrag = ProductBuyFragment.newInstance(produtoId);
                parentFragmentManager.beginTransaction()
                    .replace(R.id.container, productFrag)
                    .addToBackStack(null)
                    .commit()

            } else if (resultCode == RESULT_CANCELED) {
                val error = data.getStringExtra("errorText") as String;

                showSnack(binding.qrCodeImage, error)
            }
        }
    }
}