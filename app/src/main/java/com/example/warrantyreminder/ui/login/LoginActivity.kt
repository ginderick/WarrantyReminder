package com.example.warrantyreminder.ui.login

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.warrantyreminder.MainActivity
import com.example.warrantyreminder.R
import com.example.warrantyreminder.databinding.ActivityLoginBinding
import com.example.warrantyreminder.ui.register.RegisterActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class LoginActivity: AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        auth = Firebase.auth

        binding.btnSignUp.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
            finish()
        }

        binding.loginButton.setOnClickListener {
            doLogin()
        }






    }

    private fun doLogin() {

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

        auth.signInWithEmailAndPassword(binding.emailInput.text.toString(), binding.passwordInput.text.toString())
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    val user = auth.currentUser
                    updateUI(user)
                } else {
                    updateUI(null)
                }
            }
    }

    public override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = auth.currentUser
        updateUI(currentUser)
    }

    private fun updateUI(currentUser: FirebaseUser?) {

        if (currentUser != null) {
            startActivity(Intent(this, MainActivity::class.java))
        } else {
            // If sign in fails, display a message to the user.
            Toast.makeText(baseContext, "Authentication failed.",
                Toast.LENGTH_SHORT).show()
        }


    }
}