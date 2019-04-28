package com.aw.ontopnote.model

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.lifecycle.LiveData
import androidx.room.Update

@Dao
interface NoteDao {
    @Query("SELECT * FROM notes WHERE id = :id")
    fun getNoteById(id: String): LiveData<Note>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertNote(note: Note)

    @Query("DELETE FROM notes")
    fun deleteAllNotes()

    @Query("SELECT * FROM notes")
    fun getAllNotes(): LiveData<List<Note>>

    @Query("SELECT * FROM notes")
    fun getAllNotesList(): List<Note>

    @Query("SELECT COUNT(*) FROM notes")
    fun count(): Int

    @Query("SELECT * FROM notes LIMIT 1")
    fun getFirstNote(): LiveData<Note>

    @Update
    fun update(note: Note)
}