package com.aw.ontopnote.model

import androidx.annotation.NonNull
import androidx.core.content.ContextCompat
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.aw.ontopnote.MainApp
import com.aw.ontopnote.R
import com.aw.ontopnote.helper.Themes
import com.aw.ontopnote.model.ViewType.Companion.VISIBLE
import java.util.*

@Entity(tableName = "notes")
data class Note (
    @PrimaryKey @ColumnInfo @NonNull
    val id:String = UUID.randomUUID().toString(),

    @ColumnInfo
    var text: String,

    @ColumnInfo
    var color: Int = Themes.defaultBackground,

    @ColumnInfo
    var viewType: Int = VISIBLE,

    @ColumnInfo
    var fontSize: Int = 24,

    @ColumnInfo
    var updatedAt: Int = -1,

    @ColumnInfo
    var remoteId: String = ""
)

class ViewType {
    companion object {
        const val VISIBLE: Int = 0
        const val PARTIALLY_HIDDEN : Int = 1
        const val GONE : Int = 2
    }
}