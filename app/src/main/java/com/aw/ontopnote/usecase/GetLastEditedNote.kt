package com.aw.ontopnote.usecase

import com.aw.ontopnote.model.Note

interface GetLastEditedNote {
    fun invoke() : Note
}