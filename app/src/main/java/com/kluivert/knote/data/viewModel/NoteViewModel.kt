package com.kluivert.knote.data.viewModel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kluivert.knote.data.dtbase.KnoteDatabase
import com.kluivert.knote.data.entities.Note
import com.kluivert.knote.data.repo.NoteRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.launch

@InternalCoroutinesApi
class NoteViewModel(context: Context) : ViewModel(){

    val readNote : LiveData<List<Note>>
    var noteRepo : NoteRepository

    init {

        val noteDao = KnoteDatabase.getDatabase(context.applicationContext).noteDao()
        noteRepo = NoteRepository(noteDao)
        readNote = noteDao.readNote()

    }

   fun addNote(note: Note) {
       viewModelScope.launch (Dispatchers.IO){
           noteRepo.addNote(note)
       }
   }


    fun updateNote(note: Note){
        viewModelScope.launch (Dispatchers.IO){
            noteRepo.updateNote(note)

        }
    }

    fun deleteNote(note: Note){
        viewModelScope.launch(Dispatchers.IO) {
            noteRepo.deleteNote(note)
        }
    }

}