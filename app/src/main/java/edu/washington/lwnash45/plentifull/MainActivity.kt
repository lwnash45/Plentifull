package edu.washington.lwnash45.plentifull

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.Fragment
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.signin.SignInOptions
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth


class MainActivity : AppCompatActivity() {

    lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        FirebaseApp.initializeApp(this)

        auth = FirebaseAuth.getInstance()

        var signInFragment = SignInFragment.newInstance(auth)

        supportFragmentManager.beginTransaction().replace(R.id.fragmentFrame, signInFragment, "SIGN_IN_FRAG").commit()
    }

    override fun onStart() {
        super.onStart()
        val currentUser = auth.currentUser

    }
}
