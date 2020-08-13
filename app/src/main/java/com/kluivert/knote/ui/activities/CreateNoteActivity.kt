package com.kluivert.knote.ui.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.kluivert.knote.R

class CreateNoteActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_note)

        //window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
    }
}