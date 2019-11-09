package com.aw.ontopnote.model

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface NoteDao {
    @Query("SELECT * FROM notes WHERE id = :id")
    suspend fun getNoteById(id: String): Note

    @Query("SELECT * FROM notes WHERE id = :id")
    fun getLiveDataNoteById(id: String): LiveData<Note>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNote(note: Note)

    @Query("DELETE FROM notes")
    suspend fun deleteAllNotes()

    @Query("SELECT * FROM notes")
    fun getAllNotes(): LiveData<List<Note>>

    @Query("SELECT * FROM notes")
    suspend fun getAllNotesList(): List<Note>

    @Query("SELECT COUNT(*) FROM notes")
    suspend fun count(): Int

    @Query("SELECT * FROM notes LIMIT 1")
    suspend fun getFirstNote(): Note

    @Update
    suspend fun update(note: Note)

    @Delete
    suspend fun delete(note: Note)
}