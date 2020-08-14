package com.kluivert.knote.ui.activities

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.annotation.RequiresApi
import com.kluivert.knote.R
import com.kluivert.knote.databinding.ActivityCreateNoteBinding

class CreateNoteActivity : AppCompatActivity() {

    private lateinit var createNoteBinding: ActivityCreateNoteBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        createNoteBinding = ActivityCreateNoteBinding.inflate(layoutInflater)
        val view = createNoteBinding.root
        setContentView(view)


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        }

        createNoteBinding.imgBtnBack.setOnClickListener {
            onBackPressed()
        }
    }
}