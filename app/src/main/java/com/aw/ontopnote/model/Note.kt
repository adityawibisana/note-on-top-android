package com.aw.ontopnote.model

import androidx.core.content.ContextCompat
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.aw.ontopnote.MainApp
import com.aw.ontopnote.R
import java.util.*

@Entity(tableName = "notes")
data class Note (

    @PrimaryKey @ColumnInfo
    val id:String = UUID.randomUUID().toString(),

    @ColumnInfo
    var content: String,

    @ColumnInfo
    var color: Int = ContextCompat.getColor(MainApp.applicationContext(), R.color.blueMaterial)
)