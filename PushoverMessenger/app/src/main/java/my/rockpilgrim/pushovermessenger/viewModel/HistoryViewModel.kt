package my.rockpilgrim.pushovermessenger.viewModel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import my.rockpilgrim.pushovermessenger.app.App
import my.rockpilgrim.pushovermessenger.data.Message
import my.rockpilgrim.pushovermessenger.data.MessageDao
import my.rockpilgrim.pushovermessenger.request.Api
import java.lang.Exception

class HistoryViewModel:ViewModel(),SendMessageListener {

    sealed class State{
        object Loading : State()
        object Empty : State()
        object Sent : State()
        object Error : State()
        class History(val list:List<Message>?):State()
    }

    companion object{
        val TAG = HistoryViewModel::class.java.simpleName
    }
    private val messageDao: MessageDao = App.appDatabase.messageDao()
    val stateLiveData = MutableLiveData<State>()

    init {
        getHistoryList()
    }

    private fun getHistoryList() {
        stateLiveData.postValue(State.Loading)
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val list = App.appDatabase.messageDao().getMessages()
                if (list.isNullOrEmpty()) {
                    stateLiveData.postValue(State.Empty)
                    return@launch
                } else {
                    stateLiveData.postValue(State.History(list))
                }
            } catch (e: Exception) {
                Log.e(TAG, "getHistoryList()", e)
                stateLiveData.postValue(State.Error)
            }
        }
    }

    override fun sendMessage(message: Message) {
        stateLiveData.postValue(State.Loading)
        viewModelScope.launch(Dispatchers.IO) {
            Log.d(SendViewModel.TAG, "sendMessage() ${Thread.currentThread().name}")
            Api.create().sendMessage(message)
            stateLiveData.postValue(State.Sent)
        }
    }
}