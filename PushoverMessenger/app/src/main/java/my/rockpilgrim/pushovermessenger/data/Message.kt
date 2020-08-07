package my.rockpilgrim.pushovermessenger.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import java.util.*

@Entity(tableName = "message_table")
data class Message(

    @SerializedName("token")
    val token: String,
    @SerializedName("user")
    val user: String,
    @SerializedName("title")
    val title: String,
    @SerializedName("message")
    val message: String
){
    @PrimaryKey
    var id: String = UUID.randomUUID().toString()

    var date: String = Date().toString()
}