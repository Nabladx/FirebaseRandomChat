package com.example.firebaserandomchat

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import com.example.firebaserandomchat.databinding.ActivityMainBinding

class MainActivity : ComponentActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        bindViews()
    }

    private fun bindViews(){
        binding.startChatButton.setOnClickListener{
            val intent = Intent(this, LoadingActivity::class.java)
            startActivity(intent)
        }
    }
}