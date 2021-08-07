package uz.instat.tasklist.presentation.listener.click

import android.view.View
import androidx.recyclerview.widget.RecyclerView


interface ClickListener {

    fun onItemClicked(position: Int, v: View?)

    fun onItemLongClick(position: Int, v: View?)

}

interface ItemClickListener {
    fun onItemClicked(position: Int, view: View)
}

//Extensions
fun RecyclerView.addOnItemClickListener(onClickListener: ItemClickListener) {
    this.addOnChildAttachStateChangeListener(object :
        RecyclerView.OnChildAttachStateChangeListener {
        override fun onChildViewDetachedFromWindow(view: View) {
            view.setOnClickListener(null)
        }

        override fun onChildViewAttachedToWindow(view: View) {
            view.setOnClickListener {
                val holder = getChildViewHolder(view)
                onClickListener.onItemClicked(holder.adapterPosition, view)
            }
        }
    })
}

