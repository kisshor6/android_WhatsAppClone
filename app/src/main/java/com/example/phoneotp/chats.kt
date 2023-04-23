package com.example.phoneotp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.phoneotp.adater.ChatsAdapter
import com.example.phoneotp.databinding.FragmentChatsBinding
import com.example.phoneotp.modal.UserModal
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class chats : Fragment() {


    private var dataBase : FirebaseDatabase? = null
    private lateinit var binding: FragmentChatsBinding
    private lateinit var userList : ArrayList<UserModal>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentChatsBinding.inflate(layoutInflater)
        dataBase = FirebaseDatabase.getInstance()
        userList = ArrayList()

        binding.chatUserRecyclerView.layoutManager = LinearLayoutManager(context)
        dataBase!!.reference.child("users")
            .addValueEventListener(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    userList.clear()
                    for (snapshot1 in snapshot.children){
                        val user = snapshot1.getValue(UserModal::class.java)
                        if (user!!.uid != FirebaseAuth.getInstance().uid){
                            userList.add(user)
                        }
                    }
                    binding.chatUserRecyclerView.adapter = ChatsAdapter(context!!, userList)

                }
                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }
            })

        return binding.root
    }

}