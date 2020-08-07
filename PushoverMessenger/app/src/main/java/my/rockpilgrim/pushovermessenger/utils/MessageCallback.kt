package my.rockpilgrim.pushovermessenger.utils

import androidx.recyclerview.widget.DiffUtil
import my.rockpilgrim.pushovermessenger.data.Message

class MessageCallback:DiffUtil.ItemCallback<Message>() {
    override fun areItemsTheSame(oldItem: Message, newItem: Message): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Message, newItem: Message): Boolean {
        return oldItem.message == newItem.message
    }
}