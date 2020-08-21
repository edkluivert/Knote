package com.kluivert.knote.ui.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
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
import com.kluivert.knote.utils.DividerItemDecoration
import com.kluivert.knote.utils.KnoteListener
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.InternalCoroutinesApi
import org.koin.android.ext.android.inject
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


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

    @RequiresApi(Build.VERSION_CODES.O)
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

        mainBinding.imgProfile.setOnClickListener {
            Toasty.success(this,"Developer will work on this feature soon",
                Toast.LENGTH_SHORT,true).show()
        }

        val darkModePrefs = getSharedPreferences(getString(R.string.app_name),0)
        val editor = darkModePrefs.edit()
        val isNightModeOn : Boolean = darkModePrefs.getBoolean("NightMode",false)

        if (isNightModeOn){
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            imgTheme.setImageResource(R.drawable.sun)
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_VISIBLE
        }else{
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            imgTheme.setImageResource(R.drawable.moon)

        }

        mainBinding.imgTheme.setOnClickListener {

            if (isNightModeOn){
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                editor.putBoolean("NightMode",false)
                editor.apply()
                window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_VISIBLE
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

        mainBinding.edSearchBar.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
                filter(p0.toString())
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }
        })

        val monthName = arrayOf(
            "January", "February",
            "March", "April", "May", "June", "July",
            "August", "September", "October", "November",
            "December"
        )
        val cal = Calendar.getInstance()
        val month = monthName[cal[Calendar.MONTH]]

        mainBinding.tvMonth.setText(month)


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

    @InternalCoroutinesApi
    override fun editlistener(note: Note, position: Int) {
        noteClicked = position
        Intent(applicationContext,CreateNoteActivity::class.java).also {
            it.putExtra("isViewOrUpdate",true)
            it.putExtra("knote",note)
            startActivityForResult(it, UPDATE_NOTE_CODE)
        }
    }

    @InternalCoroutinesApi
    override suspend fun deleteListener(note: Note, position: Int) {
        noteViewModel.deleteNote(note)
    }

    fun filter(text: String) {

        val filteredCourseAry: ArrayList<Note> = ArrayList()

        val noteSearch : MutableList<Note> = notelist

        for (eachNote in noteSearch) {
            if (eachNote.noteTitle.toLowerCase(Locale.ROOT).contains(text.toLowerCase(Locale.ROOT)) || eachNote.noteContent.toLowerCase(
                    Locale.ROOT
                ).contains(text.toLowerCase(
                    Locale.ROOT
                )
                )
                || eachNote.noteContent.toLowerCase(Locale.ROOT).contains(text.toLowerCase(Locale.ROOT)) || eachNote.noteContent.toLowerCase(
                    Locale.ROOT
                ).contains(text.toLowerCase(
                    Locale.ROOT
                )
                )  ) {
                filteredCourseAry.add(eachNote)
            }
        }


        adapter.filterList(filteredCourseAry);
    }
}


