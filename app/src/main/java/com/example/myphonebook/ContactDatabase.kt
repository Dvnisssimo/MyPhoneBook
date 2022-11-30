package com.example.myphonebook

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [ContactEntity::class],
    version = 2,
    exportSchema = true
)
abstract class ContactDatabase : RoomDatabase() {

    abstract fun contactDao(): ContactDao

    companion object {

        @Volatile
        private var INSTANCE: ContactDatabase? = null

        fun getDatabase(context: Context): ContactDatabase {
            if (INSTANCE == null) {
                synchronized(this) {
                    INSTANCE = buildDatabase(context)
                }
            }
            return INSTANCE!!
        }

        private fun buildDatabase(context: Context): ContactDatabase {
            return Room.databaseBuilder(
                context.applicationContext,
                ContactDatabase::class.java,
                "contactsdb"
            )
                .allowMainThreadQueries()
                .build()
        }
    }
}