package com.kluivert.knote.ui.activities

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.kluivert.knote.R
import com.kluivert.knote.databinding.ActivityMainBinding

const val ADD_NOTE_CODE = 1
class MainActivity : AppCompatActivity() {

    private lateinit var mainBinding : ActivityMainBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainBinding = ActivityMainBinding.inflate(layoutInflater)
        val view = mainBinding.root
        setContentView(view)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        }

        mainBinding.fabAddBtn.setOnClickListener {
            startActivityForResult(
                Intent(applicationContext,CreateNoteActivity::class.java),
                ADD_NOTE_CODE
            )

        }

    }
}