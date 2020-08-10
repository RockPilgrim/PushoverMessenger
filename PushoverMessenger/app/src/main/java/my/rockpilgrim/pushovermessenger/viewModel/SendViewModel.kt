package my.rockpilgrim.pushovermessenger.viewModel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import my.rockpilgrim.pushovermessenger.app.App
import my.rockpilgrim.pushovermessenger.data.Message
import my.rockpilgrim.pushovermessenger.request.Api
import my.rockpilgrim.pushovermessenger.viewModel.SendViewModel.State.Base
import my.rockpilgrim.pushovermessenger.viewModel.SendViewModel.State.Sent
import retrofit2.HttpException

class SendViewModel : ViewModel() {

    companion object{
        val TAG = SendViewModel::class.java.simpleName
    }
    sealed class State{
        object Base : State()
        object Sent : State()
        object Error : State()
        class Edit(val userKey: String,val title: String,val message: String) : State()
    }

    private val messageDao = App.appDatabase.messageDao()
    val stateLiveData: MutableLiveData<State> = MutableLiveData(Base)

    fun sendMessage(apiToken: String, userKey: String?, title: String?, message: String?) {
        if (userKey.isNullOrEmpty() || title.isNullOrEmpty() || message.isNullOrEmpty()) {
            stateLiveData.postValue(State.Error)
            return
        }

        val messageSend = Message(token = apiToken, user = userKey, title = title, message = message)
        viewModelScope.launch(Dispatchers.IO) {
            Log.d(TAG, "sendMessage() ${Thread.currentThread().name}")
            try {

                Api.create().sendMessage(messageSend)
                stateLiveData.postValue(Sent)
                messageDao.add(messageSend)
            } catch (he: HttpException) {
                Log.e(TAG, "Wrong userKey:", he)
                stateLiveData.postValue(State.Error)
            }
        }
    }

    fun saveState(userKey: String, title: String, message: String) {
        stateLiveData.postValue(State.Edit(userKey, title, message))
    }
}