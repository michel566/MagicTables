package com.michelbarbosa.magictables.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AdapterListUpdateCallback
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.michelbarbosa.magictables.databinding.ItemMainDataBinding
import com.michelbarbosa.magictables.model.MainData
import com.michelbarbosa.magictables.ui.viewholders.MainDataViewHolder

class MainDataAdapter(
    var list: MutableList<MainData>,
    private val addItemMainCallback: (item: MainData) -> Unit,
    private val removeItemMainCallback: (item: MainData, position: Int) -> Unit,
) : RecyclerView.Adapter<MainDataViewHolder>() {

    private lateinit var mHolder: MainDataViewHolder

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainDataViewHolder {
        mHolder = MainDataViewHolder(
            ItemMainDataBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            ), parent.context,
            addItemMainCallback,
            removeItemMainCallback
        )
        return mHolder
    }

    override fun getItemCount() = list.size

    override fun onBindViewHolder(holder: MainDataViewHolder, position: Int) {
        val current = list[position]
        holder.bind(current, itemCount - 1)
        mHolder.bind(current, itemCount - 1)
    }

    fun updateWithLastItemViewHolder() {
        addCheckedItem(mHolder.getMLastItem())
    }

    fun addCheckedItem(item: MainData) {
        if (!list.contains(item)) {
            val i = list.size
            val newItems = ArrayList(list)
            newItems.add(i, item.apply { index = i })
            val changedCount = itemCount - i + 1
            updateAll(newItems)
            notifyItemRangeChanged(i, changedCount)
        }
    }

    fun addItem(
        item: MainData, nextItemCallback: (nextPos: Int) -> Unit
    ) {
        val i = list.size
        val newItems = ArrayList(list)
        newItems.add(item.apply { index = i })
        val changedCount = itemCount - i + 1
        updateAll(newItems)
        notifyItemRangeChanged(i, changedCount)
        nextItemCallback.invoke(i)
    }

    fun removeItem(position: Int, item: MainData) {
        if (position != list.size) {
            val newItems = ArrayList(list)
            newItems.removeAt(position)
            val itemChangedCount = list.size - position
            var i = 0
            newItems.forEach {
                it.apply { index = i++ }
            }
            updateAll(newItems)
            notifyItemRemoved(position)
            notifyItemRangeChanged(position, itemChangedCount)
        }
    }

    fun updateAll(newList: List<MainData>) {
        val diffCallback = MainDataDiffCallback(list, newList)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        list.clear()
        list = mutableListOf()
        list.addAll(newList)
        /**
         * Will trigger notifyRangeRemoved and/ or notifyItemRangedInserted internally
         */
        diffResult.dispatchUpdatesTo(AdapterListUpdateCallback(this))
    }

}


//
//    private var list: MutableList<MainData>,
//    private val addItemMainCallback: (item: MainData) -> Unit,
//    private val removeItemMainCallback: (Int, MainData) -> Unit,
//    ) : RecyclerView.Adapter<MainDataViewHolder>() {
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainDataViewHolder {
//        return MainDataViewHolder.inflate(
//            parent,
//            addItemMainCallback,
//            removeItemMainCallback
//        )
//    }
//
//    override fun getItemCount() = list.size
//
//    override fun onBindViewHolder(holder: MainDataViewHolder, position: Int) {
//        val current = list[position]
//        holder.bind(current)
//    }
//
//    fun removeItem(position: Int, item: MainData) {
//        val newItems = ArrayList(list)
//        newItems.removeAt(position)
//        val itemChangedCount = list.size - position
//        var i = 0
//        newItems.forEach {
//            it.apply { index = i++ }
//        }
//        updateAll(newItems)
//        notifyItemRemoved(position)
//        notifyItemRangeChanged(position, itemChangedCount)
//    }
//
//    fun addItem(item: MainData, nextItemCallback: (nextPos: Int) -> Unit) {
//        val i = list.size
//        val newItems = ArrayList(list)
//        newItems.add(i, item.apply { index = i })
//        val changedCount = itemCount - i + 1
//        updateAll(newItems)
//        notifyItemRangeChanged(i, changedCount)
//        nextItemCallback.invoke(i)
//    }
//

//
class MainDataDiffCallback(
    private val oldList: List<MainData>,
    private val newList: List<MainData>
) : DiffUtil.Callback() {

    override fun getOldListSize() = oldList.size

    override fun getNewListSize() = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int) =
        oldList[oldItemPosition].index == newList[newItemPosition].index

    override fun areContentsTheSame(oldCourse: Int, newPosition: Int) =
        oldList[oldCourse] == newList[newPosition]
}


//}