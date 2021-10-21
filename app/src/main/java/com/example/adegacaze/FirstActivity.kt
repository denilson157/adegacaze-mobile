package com.example.adegacaze

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.adegacaze.databinding.ActivityFirstBinding
import com.example.adegacaze.databinding.ActivityMainBinding

class FirstActivity : AppCompatActivity() {

    lateinit var binding: ActivityFirstBinding;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFirstBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val welcomeFrag = WelcomeFragment.newInstance();
        supportFragmentManager.beginTransaction()
            .replace(R.id.container, welcomeFrag)
            .commit()
    }
}