package com.kluivert.knote.data.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "knote_table")
data class Note(
    @PrimaryKey(autoGenerate = true)
    var id : Int,

    @ColumnInfo(name = "title")
    val noteTitle : String,

    @ColumnInfo(name = "note_content")
    val noteContent : String,

    @ColumnInfo(name = "note_image")
    val noteImage : String,

    @ColumnInfo(name = "web_link")
    val webLink : String,

    @ColumnInfo(name = "color")
    val color : Int,

    @ColumnInfo(name = "date_time")
    val dateTime : String

):Serializable