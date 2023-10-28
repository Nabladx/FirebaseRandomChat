package com.example.firebaserandomchat.model

data class Chat(
    val id: String,
    val userId: String,
    var message: String,
    val createdAt: Long,
) {
    constructor() : this("", "", "", 0L)
}