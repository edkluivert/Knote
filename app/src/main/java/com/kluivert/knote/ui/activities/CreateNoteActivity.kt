package com.kluivert.knote.ui.activities

import android.app.Activity
import android.content.Intent
import android.graphics.Typeface
import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.kluivert.knote.R
import com.kluivert.knote.data.entities.Note
import com.kluivert.knote.data.viewModel.NoteViewModel
import com.kluivert.knote.databinding.ActivityCreateNoteBinding
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.activity_create_note.*
import kotlinx.coroutines.InternalCoroutinesApi
import org.koin.android.ext.android.inject
import java.text.SimpleDateFormat
import java.util.*

@InternalCoroutinesApi
class CreateNoteActivity : AppCompatActivity() {

    private lateinit var createNoteBinding: ActivityCreateNoteBinding



    private val noteViewModel by inject<NoteViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        createNoteBinding = ActivityCreateNoteBinding.inflate(layoutInflater)
        val createview = createNoteBinding.root
        setContentView(createview)




        createNoteBinding.layoutList.findViewById<ConstraintLayout>(R.id.layoutList)

        noteEditor()

        createNoteBinding.tvDateTime.setText(SimpleDateFormat("EEEE, dd MMMMM yyyy HH:mm a",
            Locale.getDefault()).format(Date()))



        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        }

        createNoteBinding.imgBtnBack.setOnClickListener {
            onBackPressed()
        }




        createNoteBinding.imgDone.setOnClickListener {

            val title = createNoteBinding.edTitle.text.toString().trim()
            val message = createNoteBinding.edNoteContent.text.toString().trim()

            val note = Note(0,title,message,"","","",tvDateTime.text.toString())
            if (inputChecker(title,message)){

              noteViewModel.addNote(note)
              Intent().apply {
                  setResult(Activity.RESULT_OK,this)
                  finish()
              }
               Toasty.success(this,"Saved",Toast.LENGTH_SHORT,true).show()

            }else{

                Toasty.error(this,"Enter details",Toast.LENGTH_SHORT,true).show()

            }
        }


    }

   private fun inputChecker(title : String, noteContent : String):Boolean{
       return !(TextUtils.isEmpty(title) && TextUtils.isEmpty(noteContent))
   }



   private fun noteEditor(){
        val cardLayout : CardView = findViewById(R.id.layoutEditTools)
       val bottomSheetBehavior : BottomSheetBehavior<CardView> = BottomSheetBehavior.from(cardLayout)
       cardLayout.findViewById<TextView>(R.id.tvEditTools).setOnClickListener {
           if (bottomSheetBehavior.state != BottomSheetBehavior.STATE_EXPANDED){
               bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
           }else{
               bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
           }
       }

       cardLayout.findViewById<ImageView>(R.id.imgColor).setOnClickListener {


       }



    }



    
}