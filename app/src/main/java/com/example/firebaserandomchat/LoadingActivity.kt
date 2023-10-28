package com.example.firebaserandomchat

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import com.example.firebaserandomchat.databinding.ActivityLoadingBinding
import com.example.firebaserandomchat.firebase.database.FirebaseUtil.roomDB
import com.example.firebaserandomchat.model.Room
import com.example.firebaserandomchat.model.User
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import java.util.UUID


class LoadingActivity : ComponentActivity() {
    private lateinit var binding : ActivityLoadingBinding

    private lateinit var startChatLauncher: ActivityResultLauncher<Intent>

    private var currentRoom: Room? = null
    private var waitingEventListener: ValueEventListener? = null

    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        binding = ActivityLoadingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initStartChatLauncher()
        findAvaliableChatRoom()
    }

    override fun onDestroy() {
        super.onDestroy()

        currentRoom?.let { removeRoom(it) }
        waitingEventListener?.let {
            roomDB.removeEventListener(it)
        }
    }
    private fun initStartChatLauncher() {
        startChatLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){ result ->
            if (result.resultCode == RESULT_OK) {
                finish()
            }
        }
    }

    private fun findAvaliableChatRoom() {
        setProgressText("SimpleProgress")
        roomDB.addListenerForSingleValueEvent(object : ValueEventListener {
            private var isStart: Boolean = false
            override fun onDataChange(snapshot: DataSnapshot) {
                snapshot.children.forEach{
                    val room: Room? = it.getValue(Room::class.java)
                    room?.let{_room ->
                        if (room.users.size < 2){
                            isStart = true
                            enterChat(_room)
                        }
                    }
                }
                if (isStart.not()){
                    createRoom()
                }
            }

            override fun onCancelled(error: DatabaseError) {}
        })
    }

    private fun createRoom() {
        setProgressText("simplesimpleprogress")
        val roomId = UUID.randomUUID().toString()
        val userId = UUID.randomUUID().toString()
        val room = Room(roomId, mutableListOf())
        val user = User(userId)
        room.users.add(user)

        roomDB.child(roomId).setValue(room)
            .addOnCompleteListener{ task ->
                if (task.isSuccessful) {
                    currentRoom = room
                    waiting(room)
                }
            }
    }

    private fun enterChat(room: Room){
        val userId = UUID.randomUUID().toString()
        val user = User(userId)
        room.users.add(user)
        currentRoom = room
        roomDB.child(room.id).setValue(room)
            .addOnCompleteListener {task ->
                if (task.isSuccessful) {
                    startChatActivity(room.id, user)
                }
            }
    }

    private fun waiting(room: Room) {
        waitingEventListener = object :ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                snapshot.getValue(Room::class.java)?.let {
                    if (it.users.size == 2) {
                        startChatActivity(room.id, room.users[0])
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {}
        }
        roomDB.child(room.id).addValueEventListener(waitingEventListener!!)
    }

    private fun removeRoom(room: Room) {
        roomDB.child(room.id).removeValue()
    }
    private fun startChatActivity(roomId: String, currentUser: User){
        val intent = Intent(this, ChatActivity::class.java)
            .apply {
                putExtra("roomId", roomId)
                putExtra("currentUser", currentUser)
            }
        startChatLauncher.launch(intent)
    }
    private fun setProgressText(text: String) {
        binding.progressTextView.text = text
    }
}