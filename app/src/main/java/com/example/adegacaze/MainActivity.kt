package com.example.adegacaze

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.adegacaze.databinding.ActivityMainBinding

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
                    supportFragmentManager.beginTransaction().replace(R.id.container, fragPadrao)
                        .commit()
                }
            }

            true
        }
    }
}