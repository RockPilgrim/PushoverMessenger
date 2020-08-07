package my.rockpilgrim.pushovermessenger.viewModel

import my.rockpilgrim.pushovermessenger.data.Message

interface SendMessageListener {
    fun sendMessage(message: Message)
}