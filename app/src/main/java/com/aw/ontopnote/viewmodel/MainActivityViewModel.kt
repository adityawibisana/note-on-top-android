package com.aw.ontopnote.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aw.ontopnote.model.Note
import com.aw.ontopnote.usecase.GetLastEditedNote
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    val getLastEditedNote: GetLastEditedNote
) : ViewModel() {

    private val _lastEditedNote = MutableLiveData<Note>()
    val lastEditedNote: LiveData<Note> get() = _lastEditedNote

    init {
        viewModelScope.launch(Dispatchers.IO) {
            _lastEditedNote.postValue(getLastEditedNote.invoke())
        }
    }
}