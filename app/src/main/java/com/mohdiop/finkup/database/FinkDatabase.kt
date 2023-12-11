package com.mohdiop.finkup.database

import android.content.Context
import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.RenameColumn
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.AutoMigrationSpec

@Database(
    [Fink::class], version = 3, exportSchema = true,
    autoMigrations = [AutoMigration(
        from = 1,
        to = 2,
        spec = FinkDatabase.AutoMigrateFrom1To2::class
    ),
        AutoMigration(
            from = 2,
            to = 3,
            spec = FinkDatabase.AutoMigrateFrom2To3::class
        )]
)
abstract class FinkDatabase : RoomDatabase() {
    abstract fun finkDao(): FinkDao

    @RenameColumn("Fink", "title", "finkTitle")
    class AutoMigrateFrom1To2 : AutoMigrationSpec

    @RenameColumn("Fink", "content", "finkContent")
    @RenameColumn("Fink", "date", "finkDate")
    class AutoMigrateFrom2To3 : AutoMigrationSpec

    companion object {
        private const val DATABASE_NAME = "fink_database"

        @Volatile
        private var instance: FinkDatabase? = null

        fun getInstance(context: Context): FinkDatabase {
            return instance ?: synchronized(this) {
                instance ?: Room.databaseBuilder(context, FinkDatabase::class.java, DATABASE_NAME)
                    .build()
                    .also { instance = it }
            }
        }
    }
}