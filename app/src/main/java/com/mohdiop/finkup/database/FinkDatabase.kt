package com.mohdiop.finkup.database

import androidx.room.Database
import androidx.room.RoomDatabase

@Database([Fink::class], version = 1)
abstract class FinkDatabase: RoomDatabase() {
    abstract fun finkDao(): FinkDao
}