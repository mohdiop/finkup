package com.mohdiop.finkup.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface FinkDao {
    @Query("SELECT * FROM fink ORDER BY finkDate DESC")
    suspend fun getAllFinks(): List<Fink>

    @Insert
    suspend fun insertFink(fink: Fink)

    @Delete
    suspend fun deleteFink(fink: Fink)

    @Update
    suspend fun updateFink(oldFink: Fink)

    @Query(
        "SELECT * FROM fink WHERE finkContent LIKE '%'||:key||'%' OR finkTitle LIKE '%'||:key||'%'" +
                "ORDER BY finkDate DESC"
    )
    suspend fun searchFink(key: String): List<Fink>

    @Query("DELETE FROM fink")
    suspend fun truncate()
}