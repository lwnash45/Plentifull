package edu.washington.lwnash45.plentifull


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth

import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.fragment_sign_up.*


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 *
 */
class SignUpFragment : Fragment() {

    lateinit var auth: FirebaseAuth
    lateinit var db: FirebaseFirestore

    companion object {
        fun newInstance(auth: FirebaseAuth, db: FirebaseFirestore): SignUpFragment {
            val frag = SignUpFragment()
            frag.auth = auth
            frag.db = db
            return frag
        }
    }

    interface SignUpListener {
        fun onSignUpComplete()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val root = inflater.inflate(R.layout.fragment_sign_up, container, false)

        root.findViewById<View>(R.id.signUpButton).setOnClickListener {
            val firstNameView = root.findViewById<EditText>(R.id.firstName)
            val lastNameView = root.findViewById<EditText>(R.id.lastName)
            val usernameView = root.findViewById<EditText>(R.id.userName)
            val emailView = root.findViewById<EditText>(R.id.email)
            val emailConfView = root.findViewById<EditText>(R.id.confirmEmail)
            val passwordView = root.findViewById<EditText>(R.id.password)
            val passwordConfView = root.findViewById<EditText>(R.id.passwordConf)
            val phoneNumberView = root.findViewById<EditText>(R.id.phone)
            val addressView = root.findViewById<EditText>(R.id.address)
            val addressConfView = root.findViewById<EditText>(R.id.addressLine2)
            val stateView = root.findViewById<EditText>(R.id.state)
            val cityView = root.findViewById<EditText>(R.id.city)
            val zipView = root.findViewById<EditText>(R.id.zip)
            auth.createUserWithEmailAndPassword(emailView.text.toString(), passwordView.text.toString()).addOnCompleteListener {
                if (it.isSuccessful) {

                    val user = hashMapOf(("first_name" to firstNameView.text.toString()),
                        "last_name" to lastNameView.text.toString(),
                        "email" to emailView.text.toString(),
                        "phone_number" to phoneNumberView.text.toString(),
                        "username" to usernameView.text.toString())

                    db.collection("users").add(user).addOnSuccessListener {
                        (activity as SignUpListener).onSignUpComplete()
                    }.addOnFailureListener {
                        Toast.makeText(activity, "New User Creation Failed", Toast.LENGTH_LONG)
                    }

                } else {
                    Toast.makeText(activity, "Sign Up Failed", Toast.LENGTH_LONG).show()
                }
            }
        }
        return root
    }

//    @IgnoreExtraProperties
//    data class User(
//        val first_name: String,
//        val last_name: String,
//        val email: String,
//        val phone_number: String,
//        val username: String
//    )

//    @IgnoreExtraProperties
//    data class Status(var is_approved: Boolean) {
//        this.is_approved = true
//        val is_eligible_manager: Boolean = true
//        val is_reviewed_by: Boolean = true
//    }


}
