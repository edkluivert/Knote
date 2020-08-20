package com.kluivert.knote.ui.activities

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.content.ContentResolver
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.ColorStateList
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.GradientDrawable
import android.media.Image
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.text.TextUtils
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.withStyledAttributes
import androidx.core.graphics.toColor
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.kluivert.knote.R
import com.kluivert.knote.data.entities.Note
import com.kluivert.knote.data.viewModel.NoteViewModel
import com.kluivert.knote.databinding.ActivityCreateNoteBinding
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.activity_create_note.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.color_layout.*
import kotlinx.android.synthetic.main.color_layout.view.*
import kotlinx.android.synthetic.main.color_layout.view.colorDefault
import kotlinx.android.synthetic.main.layout_web.view.*
import kotlinx.coroutines.InternalCoroutinesApi
import org.koin.android.ext.android.get
import org.koin.android.ext.android.inject
import java.io.InputStream
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*
import java.util.jar.Manifest
import java.util.regex.Pattern

@InternalCoroutinesApi
class CreateNoteActivity : AppCompatActivity() {

    private lateinit var createNoteBinding: ActivityCreateNoteBinding


    @RequiresApi(Build.VERSION_CODES.M)
    private var selectedColor : String = "#ef5059"
 private var REQUEST_IMAGE_CODE_PERMISSION = 1
    private var REQUEST_CODE_SELECT_IMAGE = 2
    private var selectedImagePath : String = ""
    private var strurl : String = ""

    private val noteViewModel by inject<NoteViewModel>()

    private var alreadyAvailable : Note? = null


    @SuppressLint("ResourceAsColor")
    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        createNoteBinding = ActivityCreateNoteBinding.inflate(layoutInflater)
        val createview = createNoteBinding.root
        setContentView(createview)


        val darkModePrefs = getSharedPreferences(getString(R.string.app_name),0)
        val editor = darkModePrefs.edit()
        val isNightModeOn : Boolean = darkModePrefs.getBoolean("NightMode",false)

        if (isNightModeOn){
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_VISIBLE
        }else{
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)


        }

        createNoteBinding.layoutList.findViewById<ConstraintLayout>(R.id.layoutList)

        noteEditor()

        createNoteBinding.tvDateTime.setText(SimpleDateFormat("EEEE, dd MMMMM yyyy HH:mm a",
            Locale.getDefault()).format(Date()))



        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        }

        if (intent.getBooleanExtra("isViewOrUpdate",false)){
            alreadyAvailable = intent.getSerializableExtra("knote") as Note?
            viewOrUpdate()
        }

        createNoteBinding.imgBtnBack.setOnClickListener {
            onBackPressed()
        }







        createNoteBinding.imgDone.setOnClickListener {

            val title = createNoteBinding.edTitle.text.toString().trim()
            val message = createNoteBinding.edNoteContent.text.toString().trim()



                if (createNoteBinding.linlayoutWeb.visibility == View.VISIBLE){
                    createNoteBinding.tvUrl.text = strurl
                }

            val note = Note(0,title,message,selectedImagePath,strurl,selectedColor,tvDateTime.text.toString())

            if (alreadyAvailable != null){
                    note.id = alreadyAvailable!!.id
            }

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

    private fun viewOrUpdate(){

    createNoteBinding.edTitle.setText(alreadyAvailable!!.noteTitle)
    createNoteBinding.edNoteContent.setText(alreadyAvailable!!.noteContent)
        createNoteBinding.tvUrl.text = alreadyAvailable!!.webLink
        createNoteBinding.tvDateTime.text = alreadyAvailable!!.dateTime
        createNoteBinding.edTitle.setTextColor(Color.parseColor(alreadyAvailable!!.color))


        if (alreadyAvailable!!.noteImage.trim().isNotEmpty()){
              createNoteBinding.imgNote.setImageBitmap(BitmapFactory.decodeFile(alreadyAvailable!!.noteImage))
               createNoteBinding.imgNote.visibility = View.VISIBLE
               selectedImagePath = alreadyAvailable!!.noteImage
        }

        if (alreadyAvailable!!.webLink.trim().isNotEmpty()){
            createNoteBinding.tvUrl.text = alreadyAvailable!!.webLink
            createNoteBinding.linlayoutWeb.visibility = View.VISIBLE

        }




    }

   private fun inputChecker(title : String, noteContent : String):Boolean{
       return !(TextUtils.isEmpty(title) && TextUtils.isEmpty(noteContent))
   }



   @SuppressLint("ResourceAsColor")
   @RequiresApi(Build.VERSION_CODES.M)
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

      val imageColor =  cardLayout.findViewById<ImageView>(R.id.colorDefault)
       val imageColor1 =  cardLayout.findViewById<ImageView>(R.id.colorBlue)
       val imageColor2 =  cardLayout.findViewById<ImageView>(R.id.colorYellow)
       val imageColor3 =  cardLayout.findViewById<ImageView>(R.id.colorGreen)
       val imageColor4 =  cardLayout.findViewById<ImageView>(R.id.colorPink)

       cardLayout.findViewById<View>(R.id.viewColor).setOnClickListener {
           selectedColor = "#ef5059"
          createNoteBinding.edTitle.setTextColor(Color.parseColor(selectedColor))
           imageColor.setImageResource(R.drawable.tintdone)
           imageColor1.setImageResource(0)
           imageColor2.setImageResource(0)
           imageColor3.setImageResource(0)
           imageColor4.setImageResource(0)
           bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED

       }

       cardLayout.findViewById<View>(R.id.viewColor1).setOnClickListener {
           selectedColor = "#2196F3"
           createNoteBinding.edTitle.setTextColor(Color.parseColor(selectedColor))
           imageColor1.setImageResource(R.drawable.tintdone)
           imageColor.setImageResource(0)
           imageColor2.setImageResource(0)
           imageColor3.setImageResource(0)
           imageColor4.setImageResource(0)
           bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED

       }

       cardLayout.findViewById<View>(R.id.viewColor2).setOnClickListener {
           selectedColor = "#FFC107"
           createNoteBinding.edTitle.setTextColor(Color.parseColor(selectedColor))
           imageColor2.setImageResource(R.drawable.tintdone)
           imageColor1.setImageResource(0)
           imageColor.setImageResource(0)
           imageColor3.setImageResource(0)
           imageColor4.setImageResource(0)
           bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED

       }

       cardLayout.findViewById<View>(R.id.viewColor3).setOnClickListener {
           selectedColor = "#4CAF50"
           createNoteBinding.edTitle.setTextColor(Color.parseColor(selectedColor))
           imageColor3.setImageResource(R.drawable.tintdone)
           imageColor1.setImageResource(0)
           imageColor2.setImageResource(0)
           imageColor.setImageResource(0)
           imageColor4.setImageResource(0)

           bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
       }

       cardLayout.findViewById<View>(R.id.viewColor4).setOnClickListener {
           selectedColor = "#E91E63"
           createNoteBinding.edTitle.setTextColor(Color.parseColor(selectedColor))
           imageColor4.setImageResource(R.drawable.tintdone)
           imageColor.setImageResource(0)
           imageColor1.setImageResource(0)
           imageColor3.setImageResource(0)
           imageColor2.setImageResource(0)
           bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED

       }


       if (alreadyAvailable != null && alreadyAvailable!!.color.trim().isNotEmpty()){

           val sel = "#2196F3"
           if (Color.parseColor(alreadyAvailable!!.color).toString() == sel){
               cardLayout.findViewById<View>(R.id.viewColor1).performClick()
           }


       }

       cardLayout.findViewById<ImageView>(R.id.imgInsert).setOnClickListener {

           bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
           if (ActivityCompat.checkSelfPermission(
                   this,android.Manifest.permission.READ_EXTERNAL_STORAGE
               ) != PackageManager.PERMISSION_GRANTED){
               ActivityCompat.requestPermissions(this@CreateNoteActivity,
                   arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE),REQUEST_IMAGE_CODE_PERMISSION
               )
           }else{
               pickImage()
           }




       }

       cardLayout.findViewById<ImageView>(R.id.imgLink).setOnClickListener {
           bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED

           val mDialogView = LayoutInflater.from(this).inflate(R.layout.layout_web, null)

           val mBuilder = AlertDialog.Builder(this)
               .setView(mDialogView)
           val  mAlertDialog = mBuilder.show()

           mDialogView.tvAddLink.setOnClickListener {
               val url = mDialogView.findViewById<EditText>(R.id.edWeb)
                      url.requestFocus()
               if (url.text.toString().trim().isEmpty()){
                   Toasty.warning(this,"Please enter url",Toast.LENGTH_SHORT,true).show()
               }else if(!Patterns.WEB_URL.matcher(url.text.toString()).matches()){
                   Toasty.warning(this,"Please enter  correct url",Toast.LENGTH_SHORT,true).show()
               }else{
                   strurl = url.text.toString()
                   createNoteBinding.tvUrl.text = strurl
                   createNoteBinding.linlayoutWeb.visibility = View.VISIBLE
                   mAlertDialog.dismiss()
               }





           }
           //cancel button click of custom layout
           mDialogView.tvCancel.setOnClickListener {

               mAlertDialog.dismiss()
           }

       }



    }




    @RequiresApi(Build.VERSION_CODES.M)

    override fun onBackPressed() {
       // super.onBackPressed()
        onBackPressedDialog()

    }

    fun pickImage(){

        Intent(Intent.ACTION_GET_CONTENT).also {
            it.type = "image/*"
            startActivityForResult(it,REQUEST_CODE_SELECT_IMAGE)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE_SELECT_IMAGE){

            if (data != null){
                val imageuri = data.data

                if (imageuri != null){

                    try {

                        val inputStream : InputStream? = contentResolver.openInputStream(imageuri)
                        val bitmap : Bitmap? = BitmapFactory.decodeStream(inputStream)
                         createNoteBinding.imgNote.setImageBitmap(bitmap)
                        createNoteBinding.imgNote.visibility = View.VISIBLE
                        selectedImagePath = getImagePath(imageuri)

                    }catch (exception : Exception){
                         val message = exception.message.toString()
                         Toasty.warning(this, message,Toast.LENGTH_SHORT,true).show()
                    }
                }
            }


        }
    }

    fun getImagePath(uri : Uri):String{

        val filePath : String
         val cursor : Cursor? = contentResolver
             .query(uri,null,null,null,null)
          if (cursor == null){
              filePath = uri.path.toString()
          }else{
              cursor.moveToFirst()
              val index = cursor.getColumnIndex("_data")
              filePath = cursor.getString(index)
              cursor.close()
          }
              return filePath

    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if(requestCode == REQUEST_IMAGE_CODE_PERMISSION && grantResults.isNotEmpty()){
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    pickImage()
                }else{
                    Toasty.warning(this,"Permission denied",Toast.LENGTH_SHORT,true).show()
                }

        }

    }

    fun onBackPressedDialog(){
        val alert = AlertDialog.Builder(this)
            .setTitle("Exit the note")
            .setMessage("Do you want to exit the note?")
            .setIcon(R.drawable.ic_warning)
            .setPositiveButton("Yes"){_,_->
                Intent().apply {
                    setResult(Activity.RESULT_OK,this)
                    finish()
                }
            }
            .setNegativeButton("No"){_,_->

            }.create()
        alert.show()

    }


    
}