package com.kluivert.knote.adapter

import android.transition.Slide
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.kluivert.knote.R
import com.kluivert.knote.data.entities.SlideModel
import kotlinx.android.synthetic.main.item_slide.view.*
import java.util.zip.Inflater

class SlideAdapter(
   private var slides : List<SlideModel>

) : RecyclerView.Adapter<SlideAdapter.SlideAdapterViewHolder>(){



   inner class SlideAdapterViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SlideAdapterViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_slide,parent,false)
        return SlideAdapterViewHolder(view)
    }

    override fun getItemCount(): Int {
        return slides.size
    }

    override fun onBindViewHolder(holder: SlideAdapterViewHolder, position: Int) {

        holder.itemView.apply {
           val current = slides[position]
            tvSlideTitle.text = current.slidetitle
            tvSlideDesc.text = current.slidedesc
            imgSlide.setImageResource(current.slideimage)

        }

    }

}