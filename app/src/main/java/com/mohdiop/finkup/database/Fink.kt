package com.mohdiop.finkup.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Fink(
    @PrimaryKey(autoGenerate = true) val finkId: Long = 0,
    @ColumnInfo("finkTitle") val finkTitle: String,
    @ColumnInfo("finkContent") val finkContent: String,
    @ColumnInfo("finkDate") val finkDate: Long
)
