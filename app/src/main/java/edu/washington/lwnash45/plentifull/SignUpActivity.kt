package edu.washington.lwnash45.plentifull

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore


class SignUpActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_sign_up)

        db = FirebaseFirestore.getInstance()
        auth = FirebaseAuth.getInstance()

        findViewById<View>(R.id.signUpButton).setOnClickListener {
            val firstNameView = findViewById<EditText>(R.id.firstName)
            val lastNameView = findViewById<EditText>(R.id.lastName)
            val usernameView = findViewById<EditText>(R.id.userName)
            val emailView = findViewById<EditText>(R.id.email)
            val emailConfView = findViewById<EditText>(R.id.confirmEmail)
            val passwordView = findViewById<EditText>(R.id.password)
            val passwordConfView = findViewById<EditText>(R.id.passwordConf)
            val phoneNumberView = findViewById<EditText>(R.id.phone)
            val addressView = findViewById<EditText>(R.id.address)
            val addressConfView = findViewById<EditText>(R.id.addressLine2)
            val stateView = findViewById<EditText>(R.id.state)
            val cityView = findViewById<EditText>(R.id.city)
            val zipView = findViewById<EditText>(R.id.zip)
            auth.createUserWithEmailAndPassword(emailView.text.toString(), passwordView.text.toString()).addOnCompleteListener {
                if (it.isSuccessful) {

                    val user = hashMapOf(("first_name" to firstNameView.text.toString()),
                        "last_name" to lastNameView.text.toString(),
                        "email" to emailView.text.toString(),
                        "phone_number" to phoneNumberView.text.toString(),
                        "username" to usernameView.text.toString())

                    db.collection("contractors").add(user).addOnSuccessListener {
                        startActivity(Intent(this, MainActivity::class.java))
                    }.addOnFailureListener {
                        Toast.makeText(this, "New User Creation Failed", Toast.LENGTH_LONG)
                    }

                } else {
                    Toast.makeText(this, "Sign Up Failed", Toast.LENGTH_LONG).show()
                }
            }
        }
    }
}
