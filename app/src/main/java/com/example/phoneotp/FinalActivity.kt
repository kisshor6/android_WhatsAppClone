package com.example.phoneotp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.phoneotp.adater.ViewPagerAdapter
import com.example.phoneotp.databinding.ActivityFinalBinding

class FinalActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFinalBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFinalBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.WhatsAppToolbar)

        val adapter = ViewPagerAdapter(supportFragmentManager)
        binding.viewPager.adapter = adapter
        binding.tab.setupWithViewPager(binding.viewPager)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finishAffinity()
    }
}