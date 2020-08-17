package com.kluivert.knote.utils

import com.kluivert.knote.data.entities.Note

interface KnoteListener {

    fun listener(note : Note, position : Int)

}