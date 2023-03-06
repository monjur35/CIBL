package com.monjur.cibl

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.fragment.NavHostFragment
import com.monjur.cibl.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    var binding: ActivityMainBinding? =null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        supportActionBar?.setBackgroundDrawable(ColorDrawable(Color.parseColor("#ffffff")))
        supportActionBar?.elevation=5f

        val navHostFragment =
            (supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment?)!!
        val navController = navHostFragment.navController

    }
}