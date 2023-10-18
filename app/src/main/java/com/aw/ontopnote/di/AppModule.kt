package com.aw.ontopnote.di

import android.content.Context
import androidx.room.Room
import com.aw.ontopnote.model.NoteDao
import com.aw.ontopnote.model.NotesDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {
    @Provides
    @Singleton
    fun provideNotesDatabase(@ApplicationContext context: Context) : NotesDatabase {
        return Room.databaseBuilder(context, NotesDatabase::class.java, "Notes.db")
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    @Singleton
    fun provideNoteDao(notesDatabase: NotesDatabase) : NoteDao {
        return notesDatabase.noteDao()
    }
}