package com.example.todoapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest

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
        val loginregister = findViewById<TextView>(R.id.loginregister)
        loginregister.setOnClickListener {
            val intent = Intent(this, MainActivity2::class.java)
            startActivity(intent)
        }

        auth = FirebaseAuth.getInstance()

        fun signin(email: String, password: String) {
            auth.signInWithEmailAndPassword(email, password)
                .addOnSuccessListener { result ->
                    val user = auth.currentUser
                }
                .addOnFailureListener { exception ->
                    print(exception.localizedMessage)
                }
        }
        val email = findViewById<TextView>(R.id.email)
        val password = findViewById<TextView>(R.id.pass)
        val login = findViewById<Button>(R.id.signin)



    }
}