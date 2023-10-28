package com.example.firebaserandomchat.model

class Room (
    val id: String,
    val users: MutableList<User>,
){
    constructor() : this("", mutableListOf())
}

