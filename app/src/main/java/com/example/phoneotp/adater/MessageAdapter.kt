package com.example.phoneotp.adater

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.phoneotp.R
import com.example.phoneotp.databinding.ReceiverItemBinding
import com.example.phoneotp.databinding.SentItemLayoutBinding
import com.example.phoneotp.modal.MessageModal
import com.google.firebase.auth.FirebaseAuth

class MessageAdapter(var context: Context, var list : ArrayList<MessageModal>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var ITEM_SENT = 1
    var ITEM_RECEIVE = 1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == ITEM_SENT){
            SentViewHolder(
                LayoutInflater.from(parent.context).inflate(R.layout.sent_item_layout, parent, false)
            )
        }else{
            ReceiveViewHolder(
                LayoutInflater.from(parent.context).inflate(R.layout.receiver_item, parent, false)
            )
        }
    }

    override fun getItemViewType(position: Int): Int {
        return  if (FirebaseAuth.getInstance().uid == list[position].senderId) ITEM_SENT
        else ITEM_RECEIVE
    }

    override fun getItemCount(): Int {
       return list.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val message = list[position]
        if (holder.itemViewType == ITEM_SENT){
            val viewHolder = holder as SentViewHolder
            viewHolder.binding.sentText.text = message.message
        }else{
            val viewHolder = holder as ReceiveViewHolder
            viewHolder.binding.receiveText.text = message.message
        }
    }

    inner class SentViewHolder(view: View) : RecyclerView.ViewHolder(view){
        val binding = SentItemLayoutBinding.bind(view)

    }
    inner class ReceiveViewHolder(view: View) : RecyclerView.ViewHolder(view){
        val binding = ReceiverItemBinding.bind(view)
    }


}