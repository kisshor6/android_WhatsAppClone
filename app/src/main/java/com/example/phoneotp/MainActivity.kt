package com.example.phoneotp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.phoneotp.databinding.ActivityMainBinding
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.*
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMainBinding
    private lateinit var auth : FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auth = FirebaseAuth.getInstance()

        binding.verifyNumber.setOnClickListener {
            if (binding.phoneNumber.text.isEmpty()){
                Toast.makeText(this, "Please Enter your Mobile Number", Toast.LENGTH_LONG).show()

            }else{
                val intent = Intent(this@MainActivity, ActivityOTP::class.java)
                intent.putExtra("phoneNumber", binding.phoneNumber.text.toString())
                startActivity(intent)
            }
        }
    }

    override fun onStart() {
        super.onStart()
        if (auth.currentUser != null){
            startActivity(Intent(this, FinalActivity::class.java))
        }
    }
}