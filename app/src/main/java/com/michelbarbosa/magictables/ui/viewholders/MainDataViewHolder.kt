package com.michelbarbosa.magictables.ui.viewholders

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.core.widget.doOnTextChanged
import androidx.recyclerview.widget.RecyclerView
import com.michelbarbosa.magictables.R
import com.michelbarbosa.magictables.databinding.ItemMainDataBinding
import com.michelbarbosa.magictables.model.MainData
import com.michelbarbosa.magictables.model.Ordenation
import com.michelbarbosa.magictables.utils.getResDrawable
import com.michelbarbosa.magictables.utils.getTintDrawable
import com.michelbarbosa.magictables.utils.requestFocusRedirectScroll
import com.michelbarbosa.magictables.utils.setOnSingleClickListener
import com.michelbarbosa.magictables.utils.showColorPicker

class MainDataViewHolder(
    private var binding: ItemMainDataBinding,
    private var mContext: Context,
    private val addItemMainCallback: (item: MainData) -> Unit,
    private val removeItemMainCallback: (item: MainData, position: Int) -> Unit,
) : RecyclerView.ViewHolder(binding.root) {

    private lateinit var mNewItem: MainData
    private lateinit var mLastItem: MainData
    private var mTextColor = Color.BLACK
    private var mBgColor = Color.WHITE
    private var mText = ""
    private var mLastPosition = 0

    fun bind(item: MainData, lastPosition: Int) = with(binding) {

        mLastPosition = lastPosition
        setupBtnChangeColors(item)
        setupBtnChangeColorListeners(item)
        setupEdtMainCard(item)
        setupBtnItemRemove(item)
        setupBtnItemAdd(item)
        setupPassiveListeners()
        setupDefaults(item)
    }

    private fun setupPassiveListeners() = with(binding) {
        edItemMainCard.doOnTextChanged { text, _, _, _ ->
            text?.let {
                setOnLastItem(text.toString(), null, null)
            }
        }
    }

    var t: String = ""
    var tColor: Int = 0
    var bColor: Int = 0
    private fun setOnLastItem(text: String?, textColor: Int?, bgColor: Int?) {
        text?.let { t = it }
        textColor?.let { tColor = it }
        bgColor?.let { bColor = it }

        if (layoutPosition == mLastPosition) {
            mLastItem = MainData(
                index = layoutPosition,
                name = t,
                textColor = tColor,
                bgColor = bColor,
                justUpdate = true,
                isDummy = false,
                Ordenation.HORIZONTAL
            )
        }
    }

    fun getMLastItem() = mLastItem

    private fun setupDefaults(item: MainData) = with(binding) {
        edItemMainCard.setText(item.name)
        if (item.textColor == 0) setTextColor(Color.BLACK) else setTextColor(item.textColor)
        if (item.bgColor == 0) setBackgroundColor(Color.WHITE) else setBackgroundColor(item.bgColor)
    }

    private fun setTextColor(color: Int) = with(binding) {
        edItemMainCard.setTextColor(color)
        edItemMainCard.backgroundTintList = ColorStateList.valueOf(color)
        mTextColor = color
    }

    private fun setBackgroundColor(color: Int) = with(binding) {
        contentCard.setBackgroundColor(color)
        mBgColor = color
    }

    private fun setupBtnChangeColors(item: MainData) = with(binding) {
        when (item.ordenation) {
            Ordenation.HORIZONTAL -> {
                ivBtnChangeTextColor.setImageDrawable(
                    mContext.getTintDrawable(
                        R.drawable.baseline_text_format_24,
                        R.color.colorPrimary
                    )
                )
                ivBtnChangeItemColor.setImageDrawable(
                    mContext.getTintDrawable(
                        R.drawable.baseline_color_lens_24,
                        R.color.colorPrimary
                    )
                )
                ivBtnClearFormat.setImageDrawable(
                    mContext.getTintDrawable(
                        R.drawable.baseline_close_24,
                        R.color.colorPrimary
                    )
                )

            }

            Ordenation.VERTICAL -> {
                ivBtnChangeTextColor.setImageDrawable(
                    mContext.getTintDrawable(
                        R.drawable.baseline_text_format_24,
                        R.color.colorSecondary
                    )
                )
                ivBtnChangeItemColor.setImageDrawable(
                    mContext.getTintDrawable(
                        R.drawable.baseline_text_format_24,
                        R.color.colorSecondary
                    )
                )
                ivBtnClearFormat.setImageDrawable(
                    mContext.getTintDrawable(
                        R.drawable.baseline_close_24,
                        R.color.colorSecondary
                    )
                )
            }
        }
    }

    private fun setupBtnChangeColorListeners(item: MainData) =
        with(binding) {
            flBtnChangeTextColor.setOnSingleClickListener {
                mContext.showColorPicker {
                    setTextColor(it.color)
                    setOnLastItem(null, it.color, null)
                    addNewItem(true, item)
                }
            }

            flBtnChangeItemColor.setOnSingleClickListener {
                mContext.showColorPicker {
                    setBackgroundColor(it.color)
                    setOnLastItem(null, null, it.color)
                    addNewItem(true, item)
                }
            }

            flBtnClearFormat.setOnSingleClickListener {
                edItemMainCard.setText("")
                setTextColor(Color.BLACK)
                setBackgroundColor(Color.WHITE)
            }
        }

    private fun setupEdtMainCard(item: MainData) =
        with(binding) {
            edItemMainCard.setOnFocusChangeListener { view, onFocus ->
                ivFocusEdited.isVisible = onFocus

                if (onFocus) {
                    val resDrawable = when (item.ordenation) {
                        Ordenation.HORIZONTAL -> R.drawable.ic_outline_h_button_default
                        Ordenation.VERTICAL -> R.drawable.ic_outline_v_button_default
                    }

                    ivFocusEdited.setImageDrawable(
                        mContext.getResDrawable(resDrawable)
                    )
                }
            }
            edItemMainCard.requestFocusRedirectScroll()
        }

    private fun setupBtnItemRemove(item: MainData) =
        with(binding) {
            flBtnItemRemove.setOnSingleClickListener {
//                action = Action.REMOVED
                removeItemMainCallback.invoke(item, item.index)
            }
        }

    fun setupBtnItemAdd(item: MainData) = with(binding) {
        flBtnItemAdd.setOnSingleClickListener {
            addNewItem(false, item)
        }
    }

    private fun addNewItem(justUpdate: Boolean, item: MainData){
        setNewItem(justUpdate, item)
        addItemMainCallback.invoke(mNewItem)
    }

    private fun setNewItem(justUpdate: Boolean, item: MainData) = with(binding) {
        mText = edItemMainCard.text.toString()

        mNewItem = item.apply {
            this.name = mText
            this.bgColor = mBgColor
            this.textColor = mTextColor
            this.justUpdate = justUpdate
            this.isDummy = false
        }
    }

    companion object {

        fun inflate(
            parent: ViewGroup,
            addItemMainCallback: (item: MainData) -> Unit,
            removeItemMainCallback: (item: MainData, position: Int) -> Unit,
        ): MainDataViewHolder {
            return MainDataViewHolder(
                ItemMainDataBinding.inflate(
                    LayoutInflater.from(parent.context), parent, false
                ),
                parent.context,
                addItemMainCallback,
                removeItemMainCallback
            )
        }
    }

//
//    companion object {
//        fun inflate(
//            parent: ViewGroup
//        ): MainDataViewHolder {
//            return MainDataViewHolder(
//                ItemMainDataBinding.inflate(
//                    LayoutInflater.from(parent.context), parent, false
//                ),
//                parent.context,
//            )
//        }
//    }


    //    private var binding: ItemMainDataBinding,
//    private var mContext: Context,
//    private val addItemMainCallback: (item: MainData) -> Unit,
//    private val removeItemMainCallback: (Int, MainData) -> Unit,
//) : RecyclerView.ViewHolder(binding.root) {
//
//    private var mOnFocus = true
//    private var mTextColor = Color.BLACK
//    private var mBgColor = Color.WHITE
//    private lateinit var mCurrentItem: MainData
//
//    fun bind(
//        item: MainData
//    ) = with(binding) {
//        mCurrentItem = item
//        root.setOnFocusChangeListener { _, onFocus ->
//            mOnFocus = onFocus
//        }
//
//        setupDefaults(item)
//        setupBtnChangeColors(item)
//        setupBtnChangeColorListeners()
//        setupBtnItemRemove(item)
//        setupBtnItemAdd(item)
//        setupEdtMainCard(item)
//    }
//
//    private fun setupDefaults(item: MainData) = with(binding) {
//        if (item.textColor == 0) setTextColor(Color.BLACK) else setTextColor(item.textColor)
//        if (item.bgColor == 0) setBackgroundColor(Color.WHITE) else setBackgroundColor(item.bgColor)
//    }
//
//    private fun setupBtnItemRemove(item: MainData) =
//        with(binding) {
//            flBtnItemRemove.setOnSingleClickListener {
//                isRemovedItem = true
//                removeItemMainCallback.invoke(layoutPosition, item)
//            }
//        }
//
//    private fun setupBtnItemAdd(item: MainData) = with(binding) {
//        flBtnItemAdd.setOnSingleClickListener {
//            val newItem = item.copy().apply {
//                this.name = edItemMainCard.text.toString()
//                this.bgColor = mBgColor
//                this.textColor = mTextColor
//            }
//            isRemovedItem = false
//            addItemMainCallback.invoke(newItem)
//        }
//    }
//
//    private fun setupBtnChangeColors(item: MainData) = with(binding) {
//        when (item.ordenation) {
//            Ordenation.HORIZONTAL -> {
//                ivBtnChangeTextColor.setImageDrawable(
//                    mContext.getTintDrawable(
//                        R.drawable.baseline_text_format_24,
//                        R.color.colorPrimary
//                    )
//                )
//                ivBtnChangeItemColor.setImageDrawable(
//                    mContext.getTintDrawable(
//                        R.drawable.baseline_color_lens_24,
//                        R.color.colorPrimary
//                    )
//                )
//            }
//
//            Ordenation.VERTICAL -> {
//                ivBtnChangeTextColor.setImageDrawable(
//                    mContext.getTintDrawable(
//                        R.drawable.baseline_text_format_24,
//                        R.color.colorSecondary
//                    )
//                )
//                ivBtnChangeItemColor.setImageDrawable(
//                    mContext.getTintDrawable(
//                        R.drawable.baseline_text_format_24,
//                        R.color.colorSecondary
//                    )
//                )
//            }
//        }
//    }
//
//    private fun setupBtnChangeColorListeners() =
//        with(binding) {
//            flBtnChangeTextColor.setOnSingleClickListener {
//                mContext.showColorPicker {
//                    setTextColor(it.color)
//                    mCurrentItem.apply {
//                        bgColor = it.color
//                    }
//                }
//            }
//
//            flBtnChangeItemColor.setOnSingleClickListener {
//                mContext.showColorPicker {
//                    setBackgroundColor(it.color)
//                    mCurrentItem.apply {
//                        textColor = it.color
//                    }
//                }
//            }
//        }
//
//    private fun setupEdtMainCard(item: MainData) =
//        with(binding) {
//            edItemMainCard.setOnFocusChangeListener { view, onFocus ->
//                ivFocusEdited.isVisible = onFocus
//
//                if (onFocus) {
//                    val resDrawable = when (item.ordenation) {
//                        Ordenation.HORIZONTAL -> R.drawable.ic_outline_h_button_default
//                        Ordenation.VERTICAL -> R.drawable.ic_outline_v_button_default
//                    }
//
//                    ivFocusEdited.setImageDrawable(
//                        mContext.getResDrawable(resDrawable)
//                    )
//                } else {
//                    mCurrentItem.apply {
//                        name = edItemMainCard.text.toString()
//                    }
//                }
//            }
//            if (!isRemovedItem)
//                edItemMainCard.requestFocusRedirectScroll()
//        }
//
//    private fun setTextColor(color: Int) = with(binding) {
//        if (!isRemovedItem) {
//            edItemMainCard.setTextColor(color)
//            edItemMainCard.backgroundTintList = ColorStateList.valueOf(color)
//            mTextColor = color
//        }
//    }
//
//    private fun setBackgroundColor(color: Int) = with(binding) {
//        if (!isRemovedItem) {
//            contentCard.setBackgroundColor(color)
//            mBgColor = color
//        }
//    }
//

}