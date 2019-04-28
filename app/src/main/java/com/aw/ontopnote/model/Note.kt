package com.aw.ontopnote.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "notes")
data class Note (
    @PrimaryKey @ColumnInfo(name = "id")
    val id:String = UUID.randomUUID().toString(),
    @ColumnInfo(name = "content")
    val content: String
)