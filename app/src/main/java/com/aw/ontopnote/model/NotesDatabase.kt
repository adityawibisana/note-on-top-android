package com.aw.ontopnote.model


import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Note::class], version = 1)
abstract class NotesDatabase : RoomDatabase() {
    abstract fun noteDao(): NoteDao

    companion object {
        @Volatile private var INSTANCE: NotesDatabase? = null

        fun getInstance(context: Context): NotesDatabase =
                INSTANCE ?: synchronized(this) {
                    INSTANCE ?: buildDatabase(context).also { INSTANCE = it }
                }

        private fun buildDatabase(context: Context): NotesDatabase {
            return Room.databaseBuilder(context.applicationContext, NotesDatabase::class.java, "Notes.db")
                .fallbackToDestructiveMigration()
                .build()
        }
    }
}