package com.example.adegacaze

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.adegacaze.databinding.ActivityMainBinding
import com.example.adegacaze.view.OrdersFragment
import com.example.adegacaze.view.ProductListFragment

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)

        redirecionarUsuario()

        setContentView(binding.root)

    }

    private fun redirecionarUsuario() {
        val token = getTokenUser(this);

        if (token != null && token != "") {
            binding.bottomNavigationView.visibility = View.VISIBLE;
            tratarBottomNavigation()
        } else {
            binding.bottomNavigationView.visibility = View.GONE;

            val fragPadrao = WelcomeFragment();
            supportFragmentManager.beginTransaction().replace(R.id.container, fragPadrao)
                .commit()
        }

    }

    private fun tratarBottomNavigation() {

        val fragPadrao = UserProfileFragment.newInstance();
        supportFragmentManager.beginTransaction().replace(R.id.container, fragPadrao)
            .commit()

        binding.bottomNavigationView.setOnNavigationItemSelectedListener {

            when (it.itemId) {
                R.id.perfil -> {
                    val userFrag = UserProfileFragment.newInstance();
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.container, userFrag)
                        .commit()
                }
                R.id.receipt -> {
                    val pedidoFrag = OrdersFragment.newInstance();
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.container, pedidoFrag)
                        .commit()
                }
                else -> {
                    val fragProduct = ProductListFragment.newInstance();
                    supportFragmentManager.beginTransaction().replace(R.id.container, fragProduct)
                        .commit()
                }
            }

            true
        }
    }
}