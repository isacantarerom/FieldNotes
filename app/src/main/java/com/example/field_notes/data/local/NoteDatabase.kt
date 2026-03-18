package com.example.field_notes.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(
    entities = [NoteEntity::class],
    version = 1,
    exportSchema = false
)

abstract class NoteDatabase : RoomDatabase() {
    abstract fun noteDao() : NoteDao


    companion object {
        @Volatile
        private var INSTANCE: NoteDatabase? = null
        /*val mig = object : Migration(1, 1) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL(
                    "ALTER TABLE notes ADD COLUMN color INTEGER NOT NULL DEFAULT ${0xFFFFFFFF.toLong()}"
                )
            }
        }*/
        fun getDatabase(context: Context) : NoteDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    NoteDatabase::class.java,
                    "notes_database"
                )//.addMigrations(mig)
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}