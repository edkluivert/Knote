package com.kluivert.knote.data.dtbase

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.kluivert.knote.data.dao.NoteDao
import com.kluivert.knote.data.entities.Note
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.internal.synchronized

@InternalCoroutinesApi
@Database(entities = [Note::class], version = 1, exportSchema = false)
abstract class KnoteDatabase : RoomDatabase() {

    abstract fun noteDao() : NoteDao

    companion object{

        @Volatile
        private var INSTANCE : KnoteDatabase? = null


         fun getDatabase(context: Context) : KnoteDatabase {
             val tempInst =
                 INSTANCE

             if(tempInst != null){
                 return tempInst
             }

             synchronized(this){
                 var instance = Room.databaseBuilder(context.applicationContext,
                 KnoteDatabase::class.java,
                     "knote_database"
                     ).build()
                 INSTANCE = instance
                 return instance

             }


         }


    }

}