package com.aw.ontopnote.model.event

import com.aw.ontopnote.model.Note

data class SocketUpdateNoteEvent (val note: Note)