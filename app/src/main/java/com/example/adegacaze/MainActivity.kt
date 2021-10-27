package com.example.adegacaze

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.adegacaze.databinding.ActivityMainBinding
import com.example.adegacaze.view.ProductBuyFragment
import com.example.adegacaze.view.ProductListFragment

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        tratarBottomNavigation()

        setContentView(binding.root)

    }
    private fun tratarBottomNavigation() {

        val fragPadrao = UserProfileFragment.newInstance();
        supportFragmentManager.beginTransaction().replace(R.id.container, fragPadrao)
            .commit()

        binding.bottomNavigationView.setOnNavigationItemSelectedListener {

            when (it.itemId) {
                R.id.perfil -> {
                    val fahrenheitFrag = UserProfileFragment.newInstance();
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.container, fahrenheitFrag)
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