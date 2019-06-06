package edu.washington.lwnash45.plentifull

import android.os.AsyncTask
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import kotlin.collections.ArrayList
import android.content.Context
import android.content.Intent
import android.widget.*
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.QueryDocumentSnapshot

class JobsActivity : AppCompatActivity() {
    private lateinit var db: FirebaseFirestore
    private lateinit var auth: FirebaseAuth
    private var type: String? = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fragmented)

        db = FirebaseFirestore.getInstance()
        auth = FirebaseAuth.getInstance()

        type = intent.getStringExtra("TYPE")
        val jobTypeView = findViewById<TextView>(R.id.jobTypeView)
        setTitle(jobTypeView)

        loadJobsList(type)

        val upcomingBtn: Button = findViewById(R.id.upcoming)
        upcomingBtn.setOnClickListener {
            type = "UPCOMING"
            setTitle(jobTypeView)
            loadJobsList(type)
        }
        val pastBtn: Button = findViewById(R.id.past)
        pastBtn.setOnClickListener {
            type = "PAST"
            setTitle(jobTypeView)
            loadJobsList(type)
        }
        val newBtn: Button = findViewById(R.id.newJobTab)
        newBtn.setOnClickListener {
            type = "NEW"
            setTitle(jobTypeView)
            loadJobsList(type)
        }
        val signOutBtn: Button = findViewById(R.id.signOut)
        signOutBtn.setOnClickListener {
            var intent = Intent(this, MainActivity::class.java)
            auth.signOut()
            startActivity(intent)
        }
    }

    private fun setTitle(title: TextView) {
        when (type) {
            "PAST" -> {
                title.text = "Past Jobs"
            }
            "UPCOMING" -> {
                title.text = "Upcoming Jobs"
            }
            else -> {
                title.text = "New Jobs"
                type = "NEW"
            }
        }
    }

    private fun loadJobsList(type: String?) {
        db.collection("jobs").get().addOnSuccessListener { result ->
            val currentUser = auth.currentUser!!
            db.collection("contractors-jobs").document(currentUser.email.toString()).get().addOnSuccessListener {
                var task = AsyncDataFetch(this, type!!, it)
                task.execute(result)
            }
        }.addOnFailureListener {
            Toast.makeText(this, "Fuck!", Toast.LENGTH_LONG).show()
        }
    }

    inner class AsyncDataFetch(private val context: Context, private val type: String, private val data: DocumentSnapshot):
        AsyncTask<QuerySnapshot, Int, ArrayList<QueryDocumentSnapshot>>() {
        override fun doInBackground(vararg params: QuerySnapshot?): ArrayList<QueryDocumentSnapshot> {

            var list : ArrayList<QueryDocumentSnapshot> = arrayListOf()
            var result: QuerySnapshot? = params[0]

            var jobsArray: ArrayList<String>? = this.data["jobs"] as ArrayList<String>?
            Log.d("Yo", jobsArray.toString())

            val time = Timestamp.now().seconds

            for (doc in result!!) {
                val startTime = doc["from"] as Timestamp
                val endTime = doc["to"] as Timestamp
                when (this.type) {
                    "UPCOMING" -> if (jobsArray !== null && jobsArray.contains(doc.id) && startTime.seconds > time) {
                        list.add(doc)
                    }
                    "PAST" -> if (jobsArray !== null && jobsArray.contains(doc.id) && endTime.seconds < time) {
                        list.add(doc)
                    }
                    else -> if (jobsArray === null || !jobsArray.contains(doc.id) && startTime.seconds > time) {
                        Log.d("Yo", "please work")
                        list.add(doc)
                    }
                }
            }
            return list
        }

        override fun onPostExecute(result: ArrayList<QueryDocumentSnapshot>?) {
            var jobsAdapter = JobsAdapter(result!!, this.context, this.type)
            var manager = LinearLayoutManager(context)
            findViewById<RecyclerView>(R.id.jobsList).apply {
                setHasFixedSize(true)
                layoutManager = manager
                adapter = jobsAdapter
            }
            super.onPostExecute(result)
        }
    }

}
