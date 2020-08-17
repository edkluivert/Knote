package com.kluivert.knote.adapter

import android.content.ClipData.Item
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.PopupMenu
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.Coil
import coil.api.load
import coil.request.LoadRequest
import com.bumptech.glide.Glide
import com.kluivert.knote.R
import com.kluivert.knote.data.entities.Note
import kotlinx.android.synthetic.main.note_item.view.*
import java.io.File


class NoteAdapter(

   var noteList :List<Note> = emptyList()



) : RecyclerView.Adapter<NoteAdapter.NoteAdapterViewHolder>() {

    private val diffUtil = object : DiffUtil.ItemCallback<Note>() {
        override fun areItemsTheSame(oldItem: Note, newItem: Note): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Note, newItem: Note): Boolean {
        return  oldItem == newItem
        }



    }

     val asynDif = AsyncListDiffer(this,diffUtil)

    inner class NoteAdapterViewHolder(itemView : View):RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteAdapterViewHolder {

       return NoteAdapterViewHolder(LayoutInflater.from(parent.context).
       inflate(R.layout.note_item,parent,false
          )
       )


    }

    override fun getItemCount(): Int {
           return asynDif.currentList.size
    }

    override fun onBindViewHolder(holder: NoteAdapterViewHolder, position: Int) {


        holder.itemView.apply {
            val current = asynDif.currentList[position]
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


         setOnItemClickListener {
             onClickItemListener?.let { it(current) }

         }

        }


    }

    fun setData(noteModel : List<Note>, pos : Int){
        this.noteList = noteModel
        notifyItemInserted(pos)
    }


    private var onClickItemListener: ((Note) ->Unit)? = null
    fun setOnItemClickListener(listener : (Note) ->Unit){
        onClickItemListener = listener
    }



}
