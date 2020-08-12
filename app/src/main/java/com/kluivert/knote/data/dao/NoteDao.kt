package com.kluivert.knote.data.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.kluivert.knote.data.entities.Note

@Dao
interface NoteDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addNote(note : Note)

    @Query("SELECT * FROM knote_table ORDER BY date_time DESC")
    fun readNote() : LiveData<List<Note>>

    @Delete
    suspend  fun deleteNote(note: Note)

    @Update
    suspend fun updateNote(note: Note)

}