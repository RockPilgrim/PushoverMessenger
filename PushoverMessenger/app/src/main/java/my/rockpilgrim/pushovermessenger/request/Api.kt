package my.rockpilgrim.pushovermessenger.request

import my.rockpilgrim.pushovermessenger.data.Message
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import retrofit2.http.Body
import retrofit2.http.POST

interface Api {

    @POST("messages.json")
    suspend fun sendMessage(@Body message: Message)

    companion object {

        private const val BASE_URL = "https://api.pushover.net/1/"

        fun create(): Api =
            Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(Api::class.java)
    }

}