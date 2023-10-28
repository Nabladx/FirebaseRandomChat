package com.example.firebaserandomchat.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isInvisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.firebaserandomchat.databinding.ItemChatBinding
import com.example.firebaserandomchat.model.Chat
import com.example.firebaserandomchat.model.User

class ChatAdapter(private val currentUser: User) : ListAdapter<Chat, ChatAdapter.ViewHolder>(diffUtil) {

    inner class ViewHolder(private val binding: ItemChatBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(chat: Chat) {
            if (chat.userId == currentUser.id) {
                binding.myLayout.isInvisible = false
                binding.myNameTextView.text = "qwr"
                binding.myMessageTextView.text = chat.message
            }
            else {
                binding.otherLayout.isInvisible = false
                binding.otherNameTextView.text = "rqw"
                binding.otherMessageTextView.text = chat.message
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemChatBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(currentList[position])
    }
    companion object {
        private val diffUtil = object : DiffUtil.ItemCallback<Chat>() {
            override fun areItemsTheSame(oldItem: Chat, newItem: Chat): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Chat, newItem: Chat): Boolean {
                return oldItem == newItem
            }
        }
    }
}