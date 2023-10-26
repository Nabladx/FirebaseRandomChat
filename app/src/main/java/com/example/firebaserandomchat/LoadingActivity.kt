package com.example.firebaserandomchat

import android.os.Bundle
import androidx.activity.ComponentActivity
import com.example.firebaserandomchat.databinding.ActivityLoadingBinding
import com.example.firebaserandomchat.firebase.database.FirebaseUtil.roomDB
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener


class LoadingActivity : ComponentActivity() {
    private lateinit var binding : ActivityLoadingBinding

    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        binding = ActivityLoadingBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    private fun findAvaliableChatRoom() {
        setProgressText("SimpleProgress")
        roomDB.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

            }

            override fun onCancelled(error: DatabaseError) {
            }
        })
    }

    private fun setProgressText(text: String) {
        binding.progressTextView.text = text
    }
}