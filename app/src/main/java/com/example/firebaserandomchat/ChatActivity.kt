package com.example.firebaserandomchat

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.firebaserandomchat.adapter.ChatAdapter
import com.example.firebaserandomchat.databinding.ActivityChatBinding
import com.example.firebaserandomchat.firebase.database.FirebaseUtil.chatDB
import com.example.firebaserandomchat.firebase.database.FirebaseUtil.roomDB
import com.example.firebaserandomchat.model.Chat
import com.example.firebaserandomchat.model.Room
import com.example.firebaserandomchat.model.User
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import java.util.UUID


class ChatActivity : ComponentActivity() {
    private lateinit var binding: ActivityChatBinding

    private lateinit var roomId: String
    private lateinit var currentUser: User

    private lateinit var chatAdapter: ChatAdapter
    private var chatList: List<Chat>? = listOf()
    private lateinit var roomEventListener: ValueEventListener
    private lateinit var chatEventListener: ValueEventListener
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChatBinding.inflate(layoutInflater)
        setContentView(binding.root)

        roomId = intent.getStringExtra("roomId")!!
        currentUser = intent.getParcelableExtra("currentUser")!!

        setResult(RESULT_OK)

        bindViews()
        initChatRecyclerView()
        listenRoomDB()
        listenChatDB()
    }

    private fun bindViews() {
        binding.sendButton.setOnClickListener{
            val chatId = UUID.randomUUID().toString()
            val message = binding.messageEditText.text.toString()
            val chat = Chat(chatId, currentUser.id, message, System.currentTimeMillis())
            uploadChat(chat){
                binding.messageEditText.text.clear()
            }
        }
    }
    private fun initChatRecyclerView() {
        chatAdapter = ChatAdapter(currentUser)
        binding.chatRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.chatRecyclerView.adapter = chatAdapter
    }

    private fun listenRoomDB() {
        roomEventListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val room: Room? = snapshot.getValue(Room::class.java)

                if (room == null) {
                    Toast.makeText(this@ChatActivity, "simpletext", Toast.LENGTH_SHORT).show()
                    binding.sendButton.isEnabled = false
                }
            }

            override fun onCancelled(error: DatabaseError) {}
        }
        roomDB.child(roomId).addValueEventListener(roomEventListener)
    }

    private fun listenChatDB() {
        chatEventListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                chatList = snapshot.children.map { it.getValue(Chat::class.java)!! }
                chatAdapter.submitList(chatList)
            }

            override fun onCancelled(error: DatabaseError) {}
        }
        chatDB.child(roomId).addValueEventListener(chatEventListener)
    }

    private fun uploadChat(chat: Chat, successHandler: () -> Unit) {
        chatDB.child(roomId).push().setValue(chat)
            .addOnCompleteListener{
                successHandler()
            }
    }

    fun leaveChannel(view: View, room: Room) {
        startActivity(Intent(this, MainActivity::class.java))
//        fun onCancelled(error: DatabaseError) {}
//
//        override fun onDataChange(snapshot: DataSnapshot) {
//        val room: Room = snapshot.getValue(Room::class.java)
//        with(room) {
//            deleteFile(room)
//        }
//        if (room == null) {
//            Toast.makeText(this@ChatActivity, "simpletext", Toast.LENGTH_SHORT).show()
//            binding.sendButton.isEnabled = false
//        }

//            if (it.users.size == 2) {
//                startChatActivity(room.id, room.users[0])
//            }

    }
}
