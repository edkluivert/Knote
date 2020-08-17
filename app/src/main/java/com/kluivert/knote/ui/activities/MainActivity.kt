package com.kluivert.knote.ui.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.kluivert.knote.R
import com.kluivert.knote.adapter.NoteAdapter
import com.kluivert.knote.data.entities.Note
import com.kluivert.knote.data.viewModel.NoteViewModel
import com.kluivert.knote.databinding.ActivityMainBinding
import com.kluivert.knote.utils.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.InternalCoroutinesApi
import org.koin.android.ext.android.inject


@InternalCoroutinesApi

const val ADD_NOTE_CODE = 1
const val UPDATE_NOTE_CODE = 2
class MainActivity : AppCompatActivity(), KnoteListener {

    private lateinit var mainBinding : ActivityMainBinding
    @InternalCoroutinesApi
    private val noteViewModel by inject<NoteViewModel>()
    private lateinit var adapter : NoteAdapter
    private lateinit var constraintLayout: ConstraintLayout
    var notelist: MutableList<Note> = mutableListOf()
    private var noteClicked : Int = -1

    @SuppressLint("ResourceType")
    @InternalCoroutinesApi
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

        val darkModePrefs = getSharedPreferences(getString(R.string.app_name),0)
        val editor = darkModePrefs.edit()
        val isNightModeOn : Boolean = darkModePrefs.getBoolean("NightMode",false)

        if (isNightModeOn){
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            imgTheme.setImageResource(R.drawable.sun)
        }else{
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            imgTheme.setImageResource(R.drawable.moon)

        }

        mainBinding.imgTheme.setOnClickListener {

            if (isNightModeOn){
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                editor.putBoolean("NightMode",false)
                editor.apply()

                imgTheme.setImageResource(R.drawable.sun)
            }else{
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                editor.putBoolean("NightMode",true)
                editor.apply()

                imgTheme.setImageResource(R.drawable.moon)
            }

        }

         adapter = NoteAdapter(notelist,this)
        mainBinding.noteRecycler.adapter = adapter
        mainBinding.noteRecycler.layoutManager = LinearLayoutManager(this)
       mainBinding.noteRecycler.addItemDecoration(DividerItemDecoration(this,LinearLayoutManager.VERTICAL))

        noteViewModel.readNote.observe(this, Observer {
            if (it.isEmpty()){
                mainBinding.emptyLayout.visibility = View.VISIBLE
            }else{
                mainBinding.emptyLayout.visibility = View.GONE
            }
            adapter.updateListItems(it.toMutableList())
             adapter.notifyDataSetChanged()
        })
        mainBinding.noteRecycler.smoothScrollToPosition(0)


    }

    @InternalCoroutinesApi
    override fun listener(note: Note, position: Int) {

        noteClicked = position
        Intent(applicationContext,CreateNoteActivity::class.java).also {
            it.putExtra("isViewOrUpdate",true)
            it.putExtra("knote",note)
            startActivityForResult(it, UPDATE_NOTE_CODE)
        }
    }


}