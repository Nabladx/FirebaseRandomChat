package com.example.firebaserandomchat.firebase.database

import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

object FirebaseUtil{
    private val database: FirebaseDatabase = Firebase.database
    val roomDB: DatabaseReference = database.reference.child("Room")
    val chatDB: DatabaseReference = database.reference.child("Chat")
}