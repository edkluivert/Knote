package com.kluivert.knote.utils

import androidx.recyclerview.widget.DiffUtil
import com.kluivert.knote.data.entities.Note

class KnoteDiffUtil(
    private val oldList: MutableList<Note>,
    private val newList: MutableList<Note>
) : DiffUtil.Callback() {


    override fun getOldListSize() = oldList.size


    override fun getNewListSize() = newList.size


    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int)
            = oldList[oldItemPosition].id == newList[newItemPosition].id


    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition] == newList[newItemPosition]
    }
}