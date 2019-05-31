package edu.washington.lwnash45.plentifull

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.Fragment
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.signin.SignInOptions
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore


class MainActivity : AppCompatActivity(), SignInFragment.BeginSignUpListener, SignUpFragment.SignUpListener {

    lateinit var auth: FirebaseAuth

    lateinit var database: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        FirebaseApp.initializeApp(this)

        auth = FirebaseAuth.getInstance()

        database = FirebaseFirestore.getInstance();

        val signInFragment = SignInFragment.newInstance(auth)

        supportFragmentManager.beginTransaction().replace(R.id.fragmentFrame, signInFragment, "SIGN_IN_FRAG").commit()
    }

    override fun onSignUp() {
        //val signUpFragment
        supportFragmentManager.beginTransaction().replace(R.id.fragmentFrame, SignUpFragment.newInstance(auth, database), "SIGN_UP_FRAG").commit()
    }

    override fun onSignUpComplete() {
        supportFragmentManager.beginTransaction().replace(R.id.fragmentFrame, SignInFragment.newInstance(auth), "SIGN_IN_FRAG").commit()
    }

    override fun onStart() {
        super.onStart()
        val currentUser = auth.currentUser
        if (currentUser != null) {
            startActivity(Intent(this, FragmentedActivity::class.java))
        }
    }
}
