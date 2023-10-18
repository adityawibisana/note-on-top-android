package com.aw.ontopnote.di

import androidx.lifecycle.ViewModel
import com.aw.ontopnote.model.NoteDao
import com.aw.ontopnote.usecase.GetLastEditedNote
import com.aw.ontopnote.usecase.GetLastEditedNoteImp
import com.aw.ontopnote.viewmodel.MainActivityViewModel
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
class ViewModelModule {
    @Provides
    @ViewModelScoped
    fun provideGetLastEditedNote(noteDao: NoteDao) : GetLastEditedNote {
        return GetLastEditedNoteImp(noteDao)
    }

    @Provides
    @ViewModelScoped
    fun provideMainActivityViewModel(getLastEditedNote: GetLastEditedNote) : ViewModel {
        return MainActivityViewModel(getLastEditedNote)
    }
}