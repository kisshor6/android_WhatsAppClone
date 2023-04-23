package com.example.phoneotp.adater

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.phoneotp.ChatActivity
import com.example.phoneotp.R
import com.example.phoneotp.databinding.ContactModalBinding
import com.example.phoneotp.modal.UserModal

class ChatsAdapter(val context: Context, private val userList: ArrayList<UserModal>) : RecyclerView.Adapter<ChatsAdapter.ViewHolder>() {

    class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {
        var binding : ContactModalBinding = ContactModalBinding.bind(itemView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.contact_modal, parent, false))
    }

    override fun getItemCount(): Int {
        return userList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val user = userList[position]
        Glide.with(context).load(user.imageUrl).into(holder.binding.imgContact)
        holder.binding.contactTitle.text = user.name
        holder.binding.contactDesc.text = user.number
        holder.itemView.setOnClickListener {
            val intent = Intent(context, ChatActivity::class.java)
            intent.putExtra("uid", user.uid)
            intent.putExtra("name", user.name)
            context.startActivity(intent)
        }

    }
}