package com.kluivert.knote.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.PopupMenu
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.api.load
import com.kluivert.knote.R
import com.kluivert.knote.data.entities.Note
import com.kluivert.knote.data.viewModel.NoteViewModel
import com.kluivert.knote.databinding.NoteItemBinding
import com.kluivert.knote.ui.activities.MainActivity
import com.kluivert.knote.utils.KnoteDiffUtil
import com.kluivert.knote.utils.KnoteListener
import kotlinx.android.synthetic.main.note_item.view.*
import kotlinx.coroutines.InternalCoroutinesApi
import org.koin.android.ext.android.inject
import org.koin.java.KoinJavaComponent.inject
import java.io.File
import kotlin.coroutines.coroutineContext


class NoteAdapter(

    var noteList: MutableList<Note> = mutableListOf(),
    var listener: KnoteListener


) : RecyclerView.Adapter<NoteAdapter.NoteAdapterViewHolder>() {



    fun updateListItems(newList: MutableList<Note>) {

        val diffCallback = KnoteDiffUtil(noteList, newList)
        val diffResult = DiffUtil.calculateDiff(diffCallback)

       noteList.clear()
       noteList.addAll(newList)

        diffResult.dispatchUpdatesTo(this)
    }

    inner class NoteAdapterViewHolder(itemView : NoteItemBinding):RecyclerView.ViewHolder(itemView.root)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteAdapterViewHolder {
        val view : NoteItemBinding = NoteItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
          return NoteAdapterViewHolder(view)

      }

    override fun getItemCount(): Int {
           return noteList.size
    }


    override fun onBindViewHolder(holder: NoteAdapterViewHolder, position: Int) {


        holder.itemView.apply {
            val current = noteList[position]
            tvTitleItem.text = current.noteTitle
            tvTitleItem.setTextColor(current.color)
            tvDescItem.text = current.noteContent

            if (current.noteImage.isBlank()){
                imgContentItem.visibility = View.GONE
            }

          imgContentItem.load(File(current.noteImage))
            imgContentItem.visibility = View.VISIBLE

             tvOptionMore.setOnClickListener {

                 val popupMenu = PopupMenu(context,tvOptionMore)
                 popupMenu.menuInflater.inflate(R.menu.note_item_menu,popupMenu.menu)
                 popupMenu.setOnMenuItemClickListener { item ->
                     when(item.itemId) {
                         R.id.miEdit ->
                             Toast.makeText(context, "You Clicked : " + item.title, Toast.LENGTH_SHORT).show()
                         R.id.miDelete ->
                             Toast.makeText(context, "You Clicked : " + item.title, Toast.LENGTH_SHORT).show()
                         R.id.miShare ->
                             Toast.makeText(context, "You Clicked : " + item.title, Toast.LENGTH_SHORT).show()
                     }
                     true
                 }
                 popupMenu.show()

             }

            noteItem.setOnClickListener {
                listener.listener(noteList[position],position)
            }

         setOnItemClickListener {
             onClickItemListener?.let { it(current) }

         }

        }


    }




    fun setData(noteModel : MutableList<Note>, pos : Int){
        this.noteList = noteModel
        notifyItemInserted(pos)
    }


    private var onClickItemListener: ((Note) ->Unit)? = null
    fun setOnItemClickListener(listener : (Note) ->Unit){
        onClickItemListener = listener
    }



}
