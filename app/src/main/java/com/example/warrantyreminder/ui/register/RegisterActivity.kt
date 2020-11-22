package com.example.warrantyreminder.ui.register

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.warrantyreminder.R
import com.example.warrantyreminder.databinding.ActivityLoginBinding
import com.example.warrantyreminder.databinding.ActivityRegisterBinding
import com.example.warrantyreminder.ui.login.LoginActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class RegisterActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var binding: ActivityRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        auth = Firebase.auth

        binding.registerButton.setOnClickListener {
            registerUser()
        }


    }

    private fun registerUser() {

        if (binding.emailInput.text.toString().isEmpty()) {
            binding.emailInput.error = "Please enter email"
            binding.emailInput.requestFocus()
            return
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(binding.emailInput.text.toString()).matches()) {
            binding.emailInput.error = "Please enter valid email"
            binding.emailInput.requestFocus()
            return
        }

        if (binding.passwordInput.text.toString().isEmpty()) {
            binding.passwordInput.error = "Please enter password"
            binding.passwordInput.requestFocus()
            return
        }

        auth.createUserWithEmailAndPassword(
            binding.emailInput.text.toString(),
            binding.passwordInput.text.toString()
        )
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    startActivity(Intent(this, LoginActivity::class.java))
                    finish()
                } else {
                    Toast.makeText(
                        baseContext, "Register failed.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
    }
}