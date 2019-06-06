package edu.washington.lwnash45.plentifull

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore


class MainActivity : AppCompatActivity() {

    lateinit var auth: FirebaseAuth
    lateinit var database: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        FirebaseApp.initializeApp(this)

        auth = FirebaseAuth.getInstance()
        database = FirebaseFirestore.getInstance()

        findViewById<View>(R.id.sign_in_button).setOnClickListener {
            val emailView = findViewById<TextView>(R.id.email)
            val email = emailView.text.toString()
            val passwordView = findViewById<TextView>(R.id.passwordConf)
            val password = passwordView.text.toString()
            if (email.isNullOrEmpty() || password.isNullOrEmpty()) {
                Toast.makeText(this, "Must Enter Email and Password", Toast.LENGTH_LONG).show()
            } else {
                auth.signInWithEmailAndPassword(email, password).addOnCompleteListener {
                    if (it.isSuccessful) {
                        val intent = Intent(this, JobsActivity::class.java)
                        intent.putExtra("TYPE", "NEW")
                        startActivity(intent)
                    } else {
                        Toast.makeText(this, "Incorrect Email or Password", Toast.LENGTH_LONG).show()
                    }
                }
            }

        }

        findViewById<View>(R.id.signUpButton).setOnClickListener {
            startActivity(Intent(this, SignUpActivity::class.java))
        }
    }

    override fun onStart() {
        super.onStart()
        val currentUser = auth.currentUser
        if (currentUser != null) {
            startActivity(Intent(this, JobsActivity::class.java))
        }
    }
}
