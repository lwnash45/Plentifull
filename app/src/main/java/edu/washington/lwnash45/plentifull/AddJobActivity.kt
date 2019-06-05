package edu.washington.lwnash45.plentifull

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import org.w3c.dom.Text

class AddJobActivity : AppCompatActivity() {

    private lateinit var db: FirebaseFirestore
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.job)

        db = FirebaseFirestore.getInstance()
        auth = FirebaseAuth.getInstance()

        val id = intent.getStringExtra("ID")!!

        db.collection("jobs").document(id!!).get().addOnSuccessListener {

            var titleView = findViewById<TextView>(R.id.jobTitle)
            var payView = findViewById<TextView>(R.id.pay)
            var locationView = findViewById<TextView>(R.id.city)
            var dateView = findViewById<TextView>(R.id.date)

            titleView.text = it["job_header"].toString()
            payView.text = "$" + it["hourly_wage_to_admin"].toString() + " /Hr"
            locationView.text = "Location: " + it["location"].toString()

            var date = it["from"] as Timestamp
            dateView.text = "Date: " + date.toDate().toString()

            val addJobBtn = findViewById<Button>(R.id.addJob)
            addJobBtn.setOnClickListener {
                val currentUser = auth.currentUser!!
                val userEmail = currentUser.email.toString()
                var usersJobs: HashMap<String, ArrayList<String>> = hashMapOf()

                db.collection("contractors-jobs").document(userEmail).get().addOnSuccessListener {
                    if (it["jobs"] == null) {
                        usersJobs["jobs"] = arrayListOf(id.toString())
                    } else {
                        var jobsArray = it["jobs"]!! as ArrayList<String>
                        jobsArray.add(id)
                        usersJobs["jobs"] = jobsArray
                    }

                    db.collection("contractors-jobs").document(currentUser.email.toString()).set(usersJobs).addOnCompleteListener {
                        val intent = Intent(this, FragmentedActivity::class.java)
                        intent.putExtra("TYPE", "UPCOMING")
                        startActivity(intent)
                    }
                }
            }
        }

    }
}
