package com.example.todoapp

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest

class MainActivity2 : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main2)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        auth = FirebaseAuth.getInstance()

        // Navigation to sign-in page
        val signinBut = findViewById<TextView>(R.id.textsignin)
        signinBut.setOnClickListener {
            startActivity(Intent(this, MainActivity3::class.java))
        }

        // Inputs
        val nameInput = findViewById<EditText>(R.id.name)
        val emailInput = findViewById<EditText>(R.id.emailregister)
        val passwordInput = findViewById<EditText>(R.id.registerpwd)
        val confirmPasswordInput = findViewById<EditText>(R.id.confirm_pwd)

        // Suppose you have a register button (replace R.id.btn_register with actual id)
        val registerButton = findViewById<TextView>(R.id.button) // or MaterialButton if that's what you used
        registerButton.setOnClickListener {
            val name = nameInput.text.toString().trim()
            val email = emailInput.text.toString().trim()
            val password = passwordInput.text.toString()
            val confirmPassword = passwordInput.text.toString()

            if (name.isEmpty() || email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if(confirmPassword != password){
                Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            signup(email, password, name, {
                // Success: you can proceed to main app screen
                Toast.makeText(this, "Registered as ${auth.currentUser?.displayName}", Toast.LENGTH_SHORT).show()

                startActivity(Intent(this, MainActivity4::class.java))}, { error ->
                // Error
                Toast.makeText(this, error, Toast.LENGTH_SHORT).show()
            })
        }
    }

    private fun signup(
        email: String,
        password: String,
        name: String,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnSuccessListener {
                val user = auth.currentUser
                if (user == null) {
                    onError("User is null after signup")
                    return@addOnSuccessListener
                }
                val profile = UserProfileChangeRequest.Builder()
                    .setDisplayName(name)
                    .build()
                user.updateProfile(profile)
                    .addOnSuccessListener {
                        onSuccess()
                    }
                    .addOnFailureListener { e ->
                        onError(e.localizedMessage ?: "Failed to set display name")
                    }
            }
            .addOnFailureListener { e ->
                onError(e.localizedMessage ?: "Signup failed")
            }
    }
}