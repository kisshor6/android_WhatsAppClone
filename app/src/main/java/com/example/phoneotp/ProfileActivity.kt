package com.example.phoneotp

import android.content.Intent
import android.graphics.drawable.GradientDrawable
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.example.phoneotp.databinding.ActivityProfileBinding
import com.example.phoneotp.modal.UserModal
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import java.util.*


class ProfileActivity : AppCompatActivity() {

    private lateinit var auth : FirebaseAuth
    private lateinit var database : FirebaseDatabase
    private lateinit var storage : FirebaseStorage
    private lateinit var selectedImage : Uri

    private lateinit var binding: ActivityProfileBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setRoundedImage(binding.ProfileImage)

        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()
        storage = FirebaseStorage.getInstance()

        binding.ProfileImage.setOnClickListener {
            val intent = Intent()
            intent.action = Intent.ACTION_GET_CONTENT
            intent.type = "image/*"
            startActivityForResult(intent, 1)
        }

        binding.setupProfile.setOnClickListener {
            if (binding.username.text.isEmpty()){
                Toast.makeText(this, "Please Enter Name", Toast.LENGTH_LONG).show()
            }else{
                binding.loading.visibility = View.VISIBLE
                setUpProfile()
            }
        }
    }

    private fun setUpProfile() {
        val reference = storage.reference.child("profile").child(Date().time.toString())
        reference.putFile(selectedImage).addOnCompleteListener{
            if (it.isSuccessful){
                reference.downloadUrl.addOnSuccessListener {task ->
                    uploadInfo(task.toString())
                }
            }
        }
    }
    private fun uploadInfo(imgUrl: String) {
        val user = UserModal(
            auth.uid.toString(),
            binding.username.text.toString(),
            auth.currentUser!!.phoneNumber.toString(), imgUrl)

        database.reference.child("users")
            .child(auth.uid.toString())
            .setValue(user)
            .addOnSuccessListener {
                binding.loading.visibility = View.GONE
                Toast.makeText(this, "Profile Updated successfully", Toast.LENGTH_LONG).show()
                startActivity(Intent(this, FinalActivity::class.java))
                finish()
            }
    }
    private fun setRoundedImage(profileImage: ImageView) {
        val shape = GradientDrawable()
        shape.shape = GradientDrawable.OVAL
        shape.setColor(ContextCompat.getColor(profileImage.context, R.color.transparent))
        profileImage.background = shape
        profileImage.clipToOutline = true
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (data != null){
            if (data.data != null){
                selectedImage = data.data!!

                binding.ProfileImage.setImageURI(selectedImage)
            }
        }
    }
}