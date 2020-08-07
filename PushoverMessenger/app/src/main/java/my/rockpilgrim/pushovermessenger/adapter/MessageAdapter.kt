package my.rockpilgrim.pushovermessenger.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import my.rockpilgrim.pushovermessenger.data.Message
import my.rockpilgrim.pushovermessenger.databinding.HistoryItemBinding
import my.rockpilgrim.pushovermessenger.utils.MessageCallback
import my.rockpilgrim.pushovermessenger.viewModel.SendMessageListener

class MessageAdapter(val messageListener: SendMessageListener) : ListAdapter<Message, MessageAdapter.MessageHolder>(MessageCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageHolder {
        val root = HistoryItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MessageHolder(root, messageListener)
    }

    override fun onBindViewHolder(holder: MessageHolder, position: Int) {
        holder.bind(getItem(position))
    }

    /// class MessageHolder
    class MessageHolder(private val binding: HistoryItemBinding, val messageListener: SendMessageListener) : RecyclerView.ViewHolder(binding.root) {

        companion object{
            private val TAG = MessageHolder::class.java.simpleName
        }

        fun bind(message: Message) {
            binding.message = message
            binding.clickListener = messageListener
        }
    }
}