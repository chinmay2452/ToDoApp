package com.example.todoapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.auth.FirebaseAuth

class MainActivity3 : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main3)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Init FirebaseAuth
        auth = FirebaseAuth.getInstance()

        val loginRegister = findViewById<TextView>(R.id.loginregister)
        loginRegister.setOnClickListener {
            startActivity(Intent(this, MainActivity2::class.java))
        }

        val emailInput = findViewById<EditText>(R.id.email)
        val passwordInput = findViewById<EditText>(R.id.pass)
        val loginButton = findViewById<Button>(R.id.signin)

        fun signin(
            email: String,
            password: String,
            onSuccess: () -> Unit,
            onError: (String) -> Unit
        ) {
            auth.signInWithEmailAndPassword(email, password)
                .addOnSuccessListener {
                    val user = auth.currentUser
                    if (user != null) {
                        println("Signed in as: ${user.email}")
                        onSuccess()
                    } else {
                        onError("User is null.")
                    }
                }
                .addOnFailureListener { exception ->
                    onError(exception.localizedMessage ?: "Login failed.")
                }
        }

        loginButton.setOnClickListener {
            val email = emailInput.text.toString().trim()
            val password = passwordInput.text.toString()

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please enter both email and password.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            signin(email, password,
                onSuccess = {
                    Toast.makeText(this, "Welcome ${auth.currentUser?.displayName ?: ""}!", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this, MainActivity4::class.java))
                    finish()
                },
                onError = { error ->
                    Toast.makeText(this, error, Toast.LENGTH_SHORT).show()
                }
            )
        }
    }
}