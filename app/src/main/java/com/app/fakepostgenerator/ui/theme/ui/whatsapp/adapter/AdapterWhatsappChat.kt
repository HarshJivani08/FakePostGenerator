package com.grewon.qmaker.ui.fake_post.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.MarginLayoutParams
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.app.fakepostgenerator.R
import com.app.fakepostgenerator.databinding.LayoutWhatsappReceiveImageBinding
import com.app.fakepostgenerator.databinding.LayoutWhatsappReceiverBinding
import com.app.fakepostgenerator.databinding.LayoutWhatsappSendImageBinding
import com.app.fakepostgenerator.databinding.LayoutWhatsappSenderBinding
import com.app.fakepostgenerator.databinding.LayoutWhatsappTimeBinding
import com.app.fakepostgenerator.ui.theme.model.DataWhatsappChat
import com.bumptech.glide.Glide


class AdapterWhatsappChat(var context: Context) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val view_send = 0
    private var view_receive = 1
    private var view_time = 2
    private var view_send_only_image = 3
    private var view_receive_only_image = 4
    private var bean = ArrayList<DataWhatsappChat>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == view_send) {
            SenderViewHolder(LayoutWhatsappSenderBinding.inflate(LayoutInflater.from(parent.context), parent, false))
        } else if (viewType == view_receive) {
            ReceiverViewHolder(LayoutWhatsappReceiverBinding.inflate(LayoutInflater.from(parent.context), parent, false))
        } else if (viewType == view_time) {
            TimeViewHolder(LayoutWhatsappTimeBinding.inflate(LayoutInflater.from(parent.context), parent, false))
        } else if (viewType == view_send_only_image) {
            SenderImageViewHolder(LayoutWhatsappSendImageBinding.inflate(LayoutInflater.from(parent.context), parent, false))
        } else {
            ReceiverImageViewHolder(LayoutWhatsappReceiveImageBinding.inflate(LayoutInflater.from(parent.context), parent, false))
        }
    }

    override fun getItemViewType(position: Int): Int {
//        return super.getItemViewType(position)
        if (bean[position].type == 0) {
            return view_send
        } else if (bean[position].type == 1) {
            return view_receive
        } else if (bean[position].type == 2) {
            return view_time
        } else if (bean[position].type == 3) {
            return view_send_only_image
        } else {
            return view_receive_only_image
        }
    }

    override fun getItemCount(): Int {
        return bean.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val data = bean[position]
        // send msg with image
        if (holder is SenderViewHolder) {
            holder.text.text = data.text
            holder.time.text = data.time

            if (data.isFirst) {
                holder.main.setBackgroundResource(R.drawable.bg_send_wtsp_corner)
            } else {
                holder.main.setBackgroundResource(R.drawable.bg_send_wtsp)
                setMargins(holder.main, 0, 0, 17, 0)
            }

            if (!data.image.isNullOrEmpty()) {
                holder.image.visibility = View.VISIBLE
                Glide.with(context).load(data.image).placeholder(R.drawable.ic_image_placeholder).into(holder.image)
            } else {
                holder.image.visibility = View.GONE
            }

            if (data.isSeen) {
                holder.seenImage.setColorFilter(ContextCompat.getColor(context, R.color.whatsapp_seen_tick_color));
            } else {
                holder.seenImage.setColorFilter(ContextCompat.getColor(context, R.color.whatsapp_unseen_tick_color));
            }

            if (data.isEditCheck) {
                holder.delete.visibility = View.VISIBLE
            } else {
                holder.delete.visibility = View.GONE
            }

            holder.delete.setOnClickListener {
                removeItem(position)
            }
        }

        // send only image
        if (holder is SenderImageViewHolder) {
            holder.time.text = data.time

            if (data.isFirst) {
                holder.main.setBackgroundResource(R.drawable.bg_send_wtsp_corner)
            } else {
                holder.main.setBackgroundResource(R.drawable.bg_send_wtsp)
                setMargins(holder.main, 0, 0, 17, 0)
            }

            if (!data.image.isNullOrEmpty()) {
                holder.image.visibility = View.VISIBLE
                Glide.with(context).load(data.image).placeholder(R.drawable.ic_image_placeholder).into(holder.image)
            } else {
                holder.image.visibility = View.GONE
            }

            if (data.isSeen) {
                holder.seenImage.setColorFilter(ContextCompat.getColor(context, R.color.whatsapp_seen_tick_color));
            } else {
                holder.seenImage.setColorFilter(ContextCompat.getColor(context, R.color.white));
            }

            if (data.isEditCheck) {
                holder.delete.visibility = View.VISIBLE
            } else {
                holder.delete.visibility = View.GONE
            }

            holder.delete.setOnClickListener {
                removeItem(position)
            }
        }

        // receive
        if (holder is ReceiverViewHolder) {
            holder.text.text = data.text
            holder.time.text = data.time

            if (data.isFirst) {
                holder.main.setBackgroundResource(R.drawable.bg_receive_wtsp_corner)

            } else {
                holder.main.setBackgroundResource(R.drawable.bg_receive_wtsp)
                setMargins(holder.main, 17, 0, 0, 0)
            }

            if (!data.image.isNullOrEmpty()) {
                holder.image.visibility = View.VISIBLE
                Glide.with(context).load(data.image).placeholder(R.drawable.ic_image_placeholder).into(holder.image)
                Log.e("RECEIVER_IMAGE", "onBindViewHolder0: " + data.image)
            } else {
                Log.e("RECEIVER_IMAGE", "onBindViewHolder1: " + data.image)
                holder.image.visibility = View.GONE
            }

            if (data.isEditCheck) {
                holder.delete.visibility = View.VISIBLE
            } else {
                holder.delete.visibility = View.GONE
            }

            holder.delete.setOnClickListener {
                removeItem(position)
            }
        }

        // receive only image
        if (holder is ReceiverImageViewHolder) {
            holder.time.text = data.time

            if (data.isFirst) {
                holder.main.setBackgroundResource(R.drawable.bg_receive_wtsp_corner)
            } else {
                holder.main.setBackgroundResource(R.drawable.bg_receive_wtsp)
                setMargins(holder.main, 17, 0, 0, 0)
            }

            if (!data.image.isNullOrEmpty()) {
                holder.image.visibility = View.VISIBLE
                Glide.with(context).load(data.image).placeholder(R.drawable.ic_image_placeholder).into(holder.image)
            } else {
                holder.image.visibility = View.GONE
            }

//            if (data.isSeen) {
//                holder.seenImage.setColorFilter(ContextCompat.getColor(context, R.color.blue));
//            } else {
//                holder.seenImage.setColorFilter(ContextCompat.getColor(context, R.color.grey_66));
//            }

            if (data.isEditCheck) {
                holder.delete.visibility = View.VISIBLE
            } else {
                holder.delete.visibility = View.GONE
            }

            holder.delete.setOnClickListener {
                removeItem(position)
            }
        }

        if (holder is TimeViewHolder) {
            holder.text.text = data.day
            if (data.isEditCheck) {
                holder.delete.visibility = View.VISIBLE
            } else {
                holder.delete.visibility = View.GONE
            }
            if (data.device == "ANDROID") {
                holder.bgTime.setBackgroundResource(R.drawable.bg_wtsp_chat_time_android)
            } else {
                holder.bgTime.setBackgroundResource(R.drawable.bg_wtsp_chat_time_ios)
            }
            holder.delete.setOnClickListener {
                removeItem(position)
            }
        }
    }

    private fun setMargins(view: View, left: Int, top: Int, right: Int, bottom: Int) {
        if (view.layoutParams is MarginLayoutParams) {
            val p = view.layoutParams as MarginLayoutParams
            p.setMargins(left, top, right, bottom)
            view.requestLayout()
        }
    }

    class SenderViewHolder(itemView: LayoutWhatsappSenderBinding) : RecyclerView.ViewHolder(itemView.root) {
        var text = itemView.txtMsg
        var main = itemView.rlMain
        var time = itemView.txtTime
        var delete = itemView.imgDelete
        var image = itemView.imgSenderImage
        var seenImage = itemView.imgSeen
    }

    class SenderImageViewHolder(itemView: LayoutWhatsappSendImageBinding) : RecyclerView.ViewHolder(itemView.root) {
        var main = itemView.lMain
        var time = itemView.txtTime
        var delete = itemView.imgDelete
        var image = itemView.imgSenderImage
        var seenImage = itemView.imgSeen
    }

    class ReceiverImageViewHolder(itemView: LayoutWhatsappReceiveImageBinding) : RecyclerView.ViewHolder(itemView.root) {
        var main = itemView.rlMain
        var time = itemView.txtTime
        var delete = itemView.imgDelete
        var image = itemView.imgReceiverImage
    }

    class ReceiverViewHolder(itemView: LayoutWhatsappReceiverBinding) : RecyclerView.ViewHolder(itemView.root) {
        var main = itemView.rlMain
        var text = itemView.txtMsg
        var time = itemView.txtTime
        var delete = itemView.imgDelete
        var image = itemView.imgReceiverImage
    }

    class TimeViewHolder(itemView: LayoutWhatsappTimeBinding) : RecyclerView.ViewHolder(itemView.root) {
        var text = itemView.txtMsgTime
        var delete = itemView.imgDelete
        var bgTime = itemView.lMsgTime
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setList(arrayList: ArrayList<DataWhatsappChat>) {
        bean = arrayList
        notifyDataSetChanged()
    }

    fun addList(arrayList: ArrayList<DataWhatsappChat>) {
        bean.addAll(arrayList)
        notifyDataSetChanged()
    }

    fun removeItem(position: Int) {
        bean.removeAt(position)
        notifyDataSetChanged()
    }

    fun removeEdiIcons() {
        for (item in bean) {
            item.isEditCheck = false
        }
        notifyDataSetChanged()
    }

    fun showEditIcons() {
        for (item in bean) {
            item.isEditCheck = true
        }
        notifyDataSetChanged()
    }

    fun iosDevice() {
        for (item in bean) {
            item.device = "IOS"
        }
        notifyDataSetChanged()
    }

    fun androidDevice() {
        for (item in bean) {
            item.device = "ANDROID"
        }
        notifyDataSetChanged()
    }


    fun getChatList(): ArrayList<DataWhatsappChat> {
        return bean
    }

}