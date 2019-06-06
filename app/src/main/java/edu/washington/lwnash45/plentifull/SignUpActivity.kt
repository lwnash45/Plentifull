package edu.washington.lwnash45.plentifull

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.CheckBox
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.fragment_sign_up.*
import kotlin.collections.HashMap


class SignUpActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_sign_up)

        db = FirebaseFirestore.getInstance()
        auth = FirebaseAuth.getInstance()

        findViewById<View>(R.id.signUpButton).setOnClickListener {
            var contractor: HashMap<String, Any> = hashMapOf()
            val firstNameView: EditText = findViewById(R.id.firstName)
            contractor["first_name"] = firstNameView.text.toString()
            val lastNameView: EditText = findViewById(R.id.lastName)
            contractor["last_name"] = lastNameView.text.toString()
            val usernameView: EditText = findViewById(R.id.userName)
            contractor["username"] = usernameView.text.toString()
            val emailView: EditText = findViewById(R.id.email)
            contractor["email"] = emailView.text.toString()
            val dobView: EditText = findViewById(R.id.dob)
            contractor["date_of_birth"] = dobView.text.toString()

            val emailConfView: EditText = findViewById(R.id.confirmEmail)
            val passwordView: EditText = findViewById(R.id.password)
            val passwordConfView: EditText = findViewById(R.id.passwordConf)

            val phoneNumberView: EditText = findViewById(R.id.phone)
            contractor["phone_number"] = phoneNumberView.text.toString()

            val address: HashMap<String, Any> = hashMapOf()

            val addressView: EditText = findViewById(R.id.address)
            address["address_line_1"] = addressView.text.toString()
            val addressLine2View: EditText = findViewById(R.id.address)
            address["address_line_2"] = addressLine2View.text.toString()
            val stateView: EditText = findViewById(R.id.state)
            address["address_state"] = stateView.text.toString()
            val cityView: EditText = findViewById(R.id.city)
            address["address_city"] = cityView.text.toString()
            val zipView: EditText = findViewById(R.id.zip)
            address["address_zip"] = zipView.text.toString()

            contractor["address"] = address

            if (!findViewById<CheckBox>(R.id.termsAndConditions).isChecked) {
                Toast.makeText(this, "Please Read and Accept Terms and Conditions", Toast.LENGTH_SHORT).show()
            } else {
                when (passwordConfView.text.toString()) {
                    password.text.toString() -> CheckInputs(contractor, db,
                        emailView.text.toString(), emailConfView.text.toString(),
                        passwordView.text.toString(), auth).run()
                    else -> Toast.makeText(this@SignUpActivity, "Password and Password Conf Do Not Match!", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    inner class CheckInputs(private val con: HashMap<String, Any>, private val database: FirebaseFirestore,
                            private val email: String, private val emailConf: String,
                            private val password: String, private val authenticate: FirebaseAuth): Runnable {
        override fun run() {
            val toast = Toast.makeText(this@SignUpActivity, "All Fields Must Be Filled", Toast.LENGTH_SHORT)
            for (node in this.con) {
                when (node.key) {
                    "address" -> {
                        val address = node.value as HashMap<String, Any>
                        for (v in address) {
                            if (v.value.toString() !== "address_line_2" && v.value.toString().isNullOrEmpty()) {
                                toast.show()
                                return
                            }
                        }
                    }
                    else -> {
                        if (node.value.toString().isNullOrEmpty()) {
                            toast.show()
                            return
                        }
                    }
                }
            }
            when (email) {
                emailConf -> {
                    this.authenticate.createUserWithEmailAndPassword(email, password).addOnSuccessListener {
                        this.con["start_date"] = Timestamp.now()
                        this.con["end_date"]
                        this.con["is_active"] = true
                        this.database.collection("contractors").add(this.con).addOnSuccessListener {
                            Toast.makeText(this@SignUpActivity, "Successfully Made New Account!!", Toast.LENGTH_LONG).show()
                            this.authenticate.signOut()
                            startActivity(Intent(this@SignUpActivity, MainActivity::class.java))
                        }.addOnFailureListener {
                            Toast.makeText(this@SignUpActivity, "New User DB Creation Failed", Toast.LENGTH_LONG).show()
                        }
                    }.addOnFailureListener {
                        Toast.makeText(this@SignUpActivity, "Trouble Making Account, if password is at least 6 chararcters", Toast.LENGTH_LONG).show()
                    }
                }
                else -> {
                    Toast.makeText(this@SignUpActivity, "Emails and email conf do not match!", Toast.LENGTH_SHORT).show()
                    return
                }
            }
        }
    }
}
