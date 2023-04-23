package com.example.phoneotp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.phoneotp.adater.MessageAdapter
import com.example.phoneotp.databinding.ActivityChatBinding
import com.example.phoneotp.modal.MessageModal
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.util.Date
class ChatActivity : AppCompatActivity() {
    private lateinit var binding : ActivityChatBinding
    private lateinit var database : FirebaseDatabase

    private lateinit var sendUid : String
    private lateinit var receiveUid : String

    private lateinit var senderRoom : String
    private lateinit var receiverRoom : String

    private lateinit var list : ArrayList<MessageModal>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChatBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolBar)
        val userName = intent.getStringExtra("name")
        binding.toolBar.title = userName
        binding.toolBar.setNavigationIcon(R.drawable.arrow_back)
        binding.toolBar.setNavigationOnClickListener {
            finish()
        }

        database = FirebaseDatabase.getInstance()
        sendUid = FirebaseAuth.getInstance().uid.toString()
        receiveUid = intent.getStringExtra("uid")!!

        list = ArrayList()

        senderRoom = sendUid+receiveUid
        receiverRoom = receiveUid+sendUid

        binding.sendMessage.setOnClickListener {
            if (binding.messageBox.text.isEmpty()){
                Toast.makeText(this, "Please Enter Your Message", Toast.LENGTH_LONG).show()
            }else{
                val message = MessageModal(binding.messageBox.text.toString(), sendUid, Date().time)
                val randomKey = database.reference.push().key
                database.reference.child("chats")
                    .child(senderRoom)
                    .child("message")
                    .child(randomKey!!)
                    .setValue(message)
                    .addOnSuccessListener {
                        database.reference.child("chats")
                            .child(receiverRoom)
                            .child("message")
                            .child(randomKey)
                            .setValue(message)
                            .addOnSuccessListener {
                                binding.messageBox.text = null
                                Toast.makeText(this,"Message sent", Toast.LENGTH_LONG).show()
                            }
                    }
            }
        }

        database.reference.child("chats")
            .child(senderRoom)
            .child("message")
            .addValueEventListener(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    list.clear()
                    for (snapshot1 in snapshot.children){
                        val data = snapshot1.getValue(MessageModal::class.java)
                        list.add(data!!)
                    }
                    binding.recyclerChats.layoutManager = LinearLayoutManager(this@ChatActivity)
                    binding.recyclerChats.adapter = MessageAdapter(this@ChatActivity, list)
                }

                override fun onCancelled(error: DatabaseError) {
                   Toast.makeText(this@ChatActivity, "error", Toast.LENGTH_LONG).show()
                }
            })
    }
}