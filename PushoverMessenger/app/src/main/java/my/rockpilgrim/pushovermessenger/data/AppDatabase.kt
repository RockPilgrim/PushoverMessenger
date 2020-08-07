package my.rockpilgrim.pushovermessenger.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Message::class], version = 1,exportSchema = false)
abstract class AppDatabase:RoomDatabase() {

    abstract fun messageDao(): MessageDao

    companion object{
        @JvmStatic
        fun buildDatabase(context: Context) =
            Room.databaseBuilder(context, AppDatabase::class.java, "message_table")
                .build()
    }
}