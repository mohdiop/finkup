package com.mohdiop.finkup.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Fink(
    @PrimaryKey(autoGenerate = true) val finkId: Long = 0,
    @ColumnInfo("title") val finkTitle: String,
    @ColumnInfo("content") val finkContent: String,
    @ColumnInfo("date") val finkDate: Long
)
