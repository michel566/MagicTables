package com.michelbarbosa.magictables.utils

import android.view.View
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.michelbarbosa.magictables.ui.adapters.MainDataAdapter

fun View.requestFocusRedirectScroll() = run {
    requestFocus()
    parent.requestChildFocus(this, this)
}

fun View.setOnSingleClickListener(onClick : ((View?) -> Unit?)) =
    setOnClickListener(
        object : SingleClickListener() {
            override fun onSingleClick(v: View?) {
                onClick.invoke(v)
            }
        }
    )

fun View.setCheckedOnSingleClickListener(onClick: (Boolean?) -> Boolean) =
    setOnClickListener(
        object : SingleClickListener() {
            override fun onSingleClick(v: View?) {
                onClick.invoke(v?.isClickable)
            }
        }
    )

fun <T> RecyclerView.setItemTouchHelper(
    targetAdapter: MainDataAdapter,
    list: MutableList<T>,
    onSwipeListener: (targetAdapter: MainDataAdapter, isRemoved: Boolean) -> Unit
) {
    adapter = targetAdapter
    var isRemoved = false

    ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
        override fun onMove(
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder,
            target: RecyclerView.ViewHolder
        ): Boolean {
            // this method is called
            // when the item is moved.
            return false
        }

        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
            isRemoved = list.isEmpty()
            if (list.isNotEmpty()){
                // this method is called when we swipe our item to right direction.
                // on below line we are getting the item at a particular position.
                val deletedCourse = list[viewHolder.adapterPosition]

                // below line is to get the position
                // of the item at that position.
                val position = viewHolder.adapterPosition - 1

                // this method is called when item is swiped.
                // below line is to remove item from our array list.
                if (list.size > 1){
                    list.removeAt(viewHolder.adapterPosition)
                } else{
                    list.clear()
                }
                val itemChangedCount = list.size - position
                isRemoved = true

                // below line is to notify our item is removed from adapter.
                targetAdapter.notifyItemRemoved(viewHolder.adapterPosition)
                targetAdapter.notifyItemRangeChanged(position, itemChangedCount)

                // below line is to display our snackbar with action.
                Snackbar.make(this@setItemTouchHelper, "Item Removed", Snackbar.LENGTH_LONG)
                    .setAction("Undo") { // adding on click listener to our action of snack bar.
                        // below line is to add our item to array list with a position.
                        list.add(position, deletedCourse)

                        // below line is to notify item is
                        // added to our adapter class.
                        targetAdapter.notifyItemInserted(position)
                    }.show()
            }
            onSwipeListener.invoke(targetAdapter, isRemoved)
        } // at last we are adding this
        // to our recycler view.
    }).attachToRecyclerView(this)
}