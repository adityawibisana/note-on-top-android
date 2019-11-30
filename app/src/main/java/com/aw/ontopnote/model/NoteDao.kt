package com.aw.ontopnote.model

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface NoteDao {
    @Query("SELECT * FROM notes WHERE id = :id")
    fun getNoteById(id: String): Note

    @Query("SELECT * FROM notes WHERE id = :id")
    fun getLiveDataNoteById(id: String): LiveData<Note>

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
    fun getFirstNote(): Note

    @Query("SELECT * FROM notes ORDER BY updatedAt DESC LIMIT 1")
    fun getLastEditedNote(): Note?

    @Query("SELECT * FROM notes ORDER BY updatedAt DESC LIMIT 1")
    fun getLastEditedNoteLive(): LiveData<Note>?

    @Update
    fun update(note: Note)

    @Delete
    fun delete(note: Note)

    @Query("SELECT * FROM notes WHERE remoteId = :remoteId")
    fun getNoteByRemoteId(remoteId: String): Note
}