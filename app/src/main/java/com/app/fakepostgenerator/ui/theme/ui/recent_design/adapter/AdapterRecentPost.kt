package com.grewon.qmaker.ui.recent_design.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.app.fakepostgenerator.R
import com.app.fakepostgenerator.databinding.LayoutRecentPostBinding
import com.app.fakepostgenerator.ui.theme.model.DataRecentPost
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions


class AdapterRecentPost(var context: Context, var onPostClickListener: OnPostClickListener, var onLongClickListener: OnLongClickListerner) :
    RecyclerView.Adapter<AdapterRecentPost.MyViewHolder>() {


    var beans = ArrayList<DataRecentPost>()
    var selecctedBean = ArrayList<String>()


    companion object {
        var isSelectionOn: Boolean = false
    }

    interface OnPostClickListener {
        fun onPostClickListener(data: String, position: Int)
    }

    interface OnLongClickListerner {
        fun onPostLongClickListener(data: String, position: Int)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding=
            LayoutRecentPostBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    @SuppressLint("NotifyDataSetChanged")
    fun addFileList(arrayList: ArrayList<DataRecentPost>) {
        beans.addAll(arrayList)
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val data = beans[position]

        Glide.with(context)
            .load(data.postPath)
            .placeholder(R.drawable.ic_recent_placeholder)
            .transition(DrawableTransitionOptions.withCrossFade())
            .into(holder.post)

        holder.itemView.setOnClickListener {
            if (isSelectionOn) {
                val path = data.postPath.toString()
                if (selecctedBean.contains(path)) {
                    holder.imgCheck.isChecked = false
                    selecctedBean.remove(path)
                } else {
                    holder.imgCheck.isChecked = true
                    selecctedBean.add(path)
                }
            } else {
                onPostClickListener.onPostClickListener(data.postPath.toString(), position)
                notifyDataSetChanged()
            }
        }


        holder.imgCheck.setOnClickListener {
            val path = data.postPath.toString()
            if (selecctedBean.contains(path)) {
                holder.imgCheck.isChecked = false
                selecctedBean.remove(path)
            } else {
                holder.imgCheck.isChecked = true
                selecctedBean.add(path)
            }
            Log.e("IMG_CHECK", "onBindViewHolder: " + selecctedBean.size)
        }

        holder.imgCheck.isChecked = selecctedBean.contains(data.postPath)
        if (isSelectionOn) {
            holder.imgCheck.visibility = View.VISIBLE
        } else {
            holder.imgCheck.visibility = View.GONE
        }

        holder.itemView.setOnLongClickListener {
            isSelectionOn = true
            selecctedBean.clear()
            onLongClickListener.onPostLongClickListener(data.postPath.toString(), position)
            notifyDataSetChanged()
            holder.itemView.performClick()
            false
        }
    }

    override fun getItemCount() = beans.size


    fun selectAllItem(isSelectedAll: Boolean) {
        try {
            if (beans != null) {
                for (index in 0 until beans.size) {
                    beans.get(index).isSelected = isSelectedAll
                }
            }
            notifyDataSetChanged()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun getselection(): Boolean {
        return isSelectionOn
    }

    fun onbackPress() {
        isSelectionOn = false
        notifyDataSetChanged()
    }

    fun getselectedList(): ArrayList<String> {
        return selecctedBean
    }

    fun getmainList(): ArrayList<DataRecentPost> {
        return beans
    }

    fun updateData(data: ArrayList<DataRecentPost>) {
        beans = data
        notifyDataSetChanged()
    }

    fun removeAt(data: Int) {
        val temp = beans
        temp.removeAt(data)
    }

    class MyViewHolder(itemView: LayoutRecentPostBinding) : RecyclerView.ViewHolder(itemView.root) {
        var post = itemView.imgPost
        var imgCheck = itemView.imgCheck
    }


}