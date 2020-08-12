package com.kluivert.knote.data.repo

import androidx.lifecycle.LiveData
import com.kluivert.knote.data.dao.NoteDao
import com.kluivert.knote.data.entities.Note

class NoteRepository(var noteDao: NoteDao) {

   val readNote : LiveData<List<Note>> = noteDao.readNote()

    suspend fun addNote(note: Note){
        noteDao.addNote(note)
    }

    suspend fun deleteNote(note: Note){
        noteDao.deleteNote(note)
    }

   suspend fun updateNote(note: Note){
        noteDao.updateNote(note)
    }

}