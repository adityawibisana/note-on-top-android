package com.aw.ontopnote.viewmodel

import com.aw.ontopnote.MainApp
import com.aw.ontopnote.model.Note
import com.aw.ontopnote.model.NoteRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class MainActivityViewModel : BaseViewModel() {
    suspend fun getLastEditedNote (): Note? {
        return withContext(Dispatchers.Default) {
            return@withContext NoteRepository.getLastEditedNote(MainApp.applicationContext())
                ?: NoteRepository.getOrCreateFirstNote(MainApp.applicationContext())
        }
    }
}