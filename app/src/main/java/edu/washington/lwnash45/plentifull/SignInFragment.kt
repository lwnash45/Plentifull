package edu.washington.lwnash45.plentifull


import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 *
 */
class SignInFragment : Fragment() {

    lateinit var auth: FirebaseAuth

    companion object {
        fun newInstance(auth: FirebaseAuth): SignInFragment {
            val frag = SignInFragment()
            frag.auth = auth
            return frag
        }
    }

    interface BeginSignUpListener {
        fun onSignUp()
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment

        val root = inflater.inflate(R.layout.fragment_sign_in, container, false)

        root.findViewById<View>(R.id.sign_in_button).setOnClickListener {
            val emailView = root.findViewById<TextView>(R.id.email)
            val email = emailView.text.toString()
            val passwordView = root.findViewById<TextView>(R.id.passwordConf)
            val password = passwordView.text.toString()
            if (email.isNullOrEmpty() || password.isNullOrEmpty()) {
                Toast.makeText(activity, "Must Enter Email and Password", Toast.LENGTH_LONG).show()
            } else {
                auth.signInWithEmailAndPassword(email, password).addOnCompleteListener {
                    if (it.isSuccessful) {
                        //val intent = Intent(root.context, FragmentedActivity::class.java)
                        startActivity(Intent(activity, FragmentedActivity::class.java))
                    } else {
                        Toast.makeText(activity, "Incorrect Email or Password", Toast.LENGTH_LONG).show()
                    }
                }
            }

        }

        root.findViewById<View>(R.id.signUpButton).setOnClickListener {
            (activity as BeginSignUpListener).onSignUp()
        }


        return root
    }


}
