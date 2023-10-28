package com.example.firebaserandomchat

import android.os.Bundle
import androidx.activity.ComponentActivity
import com.example.firebaserandomchat.databinding.ActivityChatBinding
import com.example.firebaserandomchat.databinding.ActivityLoadingBinding
import com.example.firebaserandomchat.model.User

class ChatActivity : ComponentActivity() {
    private lateinit var binding: ActivityChatBinding

    private lateinit var roomId: String
    private lateinit var currentUser: User

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChatBinding.inflate(layoutInflater)
        setContentView(binding.root)

        roomId = intent.getStringExtra("roomId")!!
        currentUser = intent.getParcelableExtra("currentUser")!!

        setResult(RESULT_OK)
    }
}