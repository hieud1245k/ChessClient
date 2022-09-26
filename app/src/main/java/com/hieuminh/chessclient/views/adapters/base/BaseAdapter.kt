package com.hieuminh.chessclient.views.adapters.base

import android.annotation.SuppressLint
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import java.util.*

abstract class BaseAdapter<T> : RecyclerView.Adapter<BaseAdapter<T>.BaseViewHolder>() {
    protected var dataList: MutableList<T> = ArrayList()
    protected var itemEventListeners: MutableList<ItemEventListener<T>> = ArrayList()
    internal var itemEventListener: ItemEventListener<T>? = null

    @SuppressLint("NotifyDataSetChanged")
    open fun updateData(list: MutableList<T>?, isReload: Boolean = true) {
        list?.let {
            dataList = it
            if (isReload) {
                this.notifyDataSetChanged()
            }
        }
    }

    override fun onViewDetachedFromWindow(holder: BaseViewHolder) {
        this.itemEventListeners.clear()
        super.onViewDetachedFromWindow(holder)
    }

    fun resetData() {
        updateData(arrayListOf())
    }

    open fun updateData(list: MutableList<T>?, position: Int) {
        list?.let {
            dataList = it
            this.notifyItemChanged(position)
        }
    }

    open fun removeData(list: MutableList<T>?, position: Int) {
        list?.let {
            dataList = it
            this.notifyItemRemoved(position)
        }
    }

    fun swap(firstPosition: Int, secondPosition: Int, isReload: Boolean = false) {
        Collections.swap(dataList, firstPosition, secondPosition)
        if (isReload) {
            listOf(firstPosition, secondPosition).forEach {
                updateData(dataList, it)
            }
        } else {
            notifyItemMoved(firstPosition, secondPosition)
        }
    }

    fun setItemListener(itemEventListener: ItemEventListener<T>?) {
        this.itemEventListener = itemEventListener
    }

    fun addItemListener(itemEventListener: ItemEventListener<T>) {
        this.itemEventListeners.add(itemEventListener)
    }

    fun removeItemListener(itemEventListener: ItemEventListener<T>): Boolean {
        return this.itemEventListeners.remove(itemEventListener)
    }

    fun dataListRemoveAt(position: Int) {
        dataList.removeAt(position)
        this.notifyItemRemoved(position)
    }

    fun isEmptyData(): Boolean {
        return dataList.isEmpty()
    }

    fun getList(): MutableList<T> {
        return dataList
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        holder.bind(dataList[position])
    }

    abstract inner class BaseViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        protected val dataItem: T
            get() = dataList[layoutPosition]

        init {
            itemView.setOnClickListener {
                itemEventListener?.onItemClick(dataList[layoutPosition], layoutPosition)
                itemEventListeners.forEach { it.onItemClick(dataList[layoutPosition], layoutPosition) }
            }
        }

        abstract fun bind(data: T)
    }

    inner class EmptyViewHolder(itemView: View) : BaseViewHolder(itemView) {
        override fun bind(data: T) = Unit
    }

    interface ItemEventListener<T> {
        fun onItemClick(item: T, position: Int)
    }
}
