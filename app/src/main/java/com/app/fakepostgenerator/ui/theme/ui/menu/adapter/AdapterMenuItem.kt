package com.grewon.qmaker.ui.menu.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.app.fakepostgenerator.databinding.LayoutMenuBinding
import com.app.fakepostgenerator.ui.theme.model.DataMenu


class AdapterMenuItem(var context: Context, var onMenuClickListener: OnMenuClickListener) :
    RecyclerView.Adapter<AdapterMenuItem.MyViewHolder>() {

    private var beans = ArrayList<DataMenu>()
    private var lastAnimatedPosition = -1
    private var lastPosition:Int ? = -1


    interface OnMenuClickListener {
        fun onMenuItemClick(data: DataMenu, position: Int)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {

        val binding= LayoutMenuBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }


    @SuppressLint("NotifyDataSetChanged")
    fun addMenuList(arrayList: ArrayList<DataMenu>) {
        beans.addAll(arrayList)
        notifyDataSetChanged()
    }

    @SuppressLint("RecyclerView")
    override fun onBindViewHolder(holder: MyViewHolder,  position: Int) {
        val data = beans[position]

        holder.image.setImageResource(data.menuImage)
        holder.txtMenu.setText(data.menuText)

        holder.itemView.setOnClickListener {
            onMenuClickListener.onMenuItemClick(data, position)
        }
    }

    override fun getItemCount() = beans.size

    class MyViewHolder(itemView: LayoutMenuBinding) : RecyclerView.ViewHolder(itemView.root) {
        var image = itemView.imgMenu
        var txtMenu = itemView.txtMenu
    }

}