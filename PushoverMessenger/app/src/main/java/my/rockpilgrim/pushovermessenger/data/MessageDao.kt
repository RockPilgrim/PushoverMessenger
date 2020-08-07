package my.rockpilgrim.pushovermessenger.data

import androidx.room.*

@Dao
interface MessageDao {

    @Query("SELECT * FROM message_table")
    suspend fun getMessages(): List<Message>?

    @Insert
    suspend fun add(message: Message)

    @Delete
    suspend fun delete(message: Message)

    @Update
    suspend fun update(message: Message)
}