package com.example.phoneotp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.phoneotp.databinding.ActivityOtpBinding
import com.google.firebase.FirebaseException
import com.google.firebase.auth.*
import java.util.concurrent.TimeUnit

class ActivityOTP : AppCompatActivity() {

    private lateinit var auth : FirebaseAuth
    private lateinit var binding : ActivityOtpBinding
    private lateinit var verificationID : String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOtpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.progresBar.visibility = View.VISIBLE

        auth = FirebaseAuth.getInstance()
        binding.verifyOTP.setOnClickListener {
            if (binding.otpNumber.text.isEmpty()){

                Toast.makeText(this, "Please Enter OTP", Toast.LENGTH_LONG).show()
            }else{

                binding.progresBar.visibility = View.VISIBLE
                val credential = PhoneAuthProvider.getCredential(verificationID, binding.otpNumber.text.toString())
                signInWithPhoneAuthCredential(credential)
            }
        }

        val phoneNumber = "+977"+intent.getStringExtra("phoneNumber")

        val options = PhoneAuthOptions.newBuilder(auth)
            .setPhoneNumber(phoneNumber)       // Phone number to verify
            .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
            .setActivity(this)                 // Activity (for callback binding)
            .setCallbacks(callbacks)          // OnVerificationStateChangedCallbacks
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)
    }

    private val callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        override fun onVerificationCompleted(credential: PhoneAuthCredential) {
            binding.progresBar.visibility = View.GONE
            signInWithPhoneAuthCredential(credential)
        }

        override fun onVerificationFailed(e: FirebaseException) {
            Toast.makeText(this@ActivityOTP, "Try again", Toast.LENGTH_LONG).show()
            Log.d("TAG", "onVerificationFailed: ${e.toString()}")
            startActivity(Intent(this@ActivityOTP, MainActivity::class.java))

        }

        override fun onCodeSent(p0: String, token: PhoneAuthProvider.ForceResendingToken) {
            binding.progresBar.visibility = View.GONE
            Toast.makeText(this@ActivityOTP, "Code Received", Toast.LENGTH_LONG).show()
            verificationID = p0
        }
    }

    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {

                    // Sign in success, update UI with the signed-in user's information
                    startActivity(Intent(this@ActivityOTP, ProfileActivity::class.java))
                    Toast.makeText(this, "Authentication Successful !", Toast.LENGTH_LONG).show()
                    finish()

                } else {
                    // Sign in failed, display a message and update the UI
                    Toast.makeText(this, "Failed ${task.exception} ", Toast.LENGTH_LONG).show()
                }
            }
    }
}