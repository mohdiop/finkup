package com.mohdiop.finkup.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database([Fink::class], version = 1)
abstract class FinkDatabase: RoomDatabase() {
    abstract fun finkDao(): FinkDao

    companion object{
        private const val DATABASE_NAME = "fink_database"
        @Volatile
        private var instance: FinkDatabase? = null

        fun getInstance(context: Context) : FinkDatabase{
            return instance?: synchronized(this){
                instance ?: Room.databaseBuilder(context,FinkDatabase::class.java, DATABASE_NAME)
                    .build()
                    .also { instance = it }
            }
        }
    }
}