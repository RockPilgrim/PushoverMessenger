package my.rockpilgrim.pushovermessenger.app

import android.app.Application
import android.content.Context
import android.util.Log
import my.rockpilgrim.pushovermessenger.data.AppDatabase
import my.rockpilgrim.pushovermessenger.data.MessageDao

class App:Application() {

    companion object{
        lateinit var appDatabase: AppDatabase
        private val TAG = App::class.java.simpleName

    }


    override fun onCreate() {
        super.onCreate()
        Log.i(TAG,"onCreate()")
        appDatabase = initAppDatabase()
    }

    private fun initAppDatabase(): AppDatabase {
        Log.i(TAG,"initAppDatabase()")
        return AppDatabase.buildDatabase(applicationContext)
    }

}