package edu.washington.lwnash45.plentifull

import android.os.AsyncTask
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.widget.ListAdapter
import android.widget.ListView
import android.widget.Toast
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import java.util.*
import kotlin.collections.ArrayList
import android.content.Context
import com.google.firebase.firestore.QueryDocumentSnapshot

class FragmentedActivity : AppCompatActivity() {
    private lateinit var db: FirebaseFirestore
    private lateinit var snapshot: QuerySnapshot
    private lateinit var jobsList: ArrayList<QueryDocumentSnapshot>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fragmented)
        db = FirebaseFirestore.getInstance()
        loadJobsList("new")
    }

    private fun loadJobsList(type: String) {
        db.collection("jobs").get().addOnSuccessListener { result ->
            snapshot = result
            var task = AsyncDataFetch(this)
            task.execute(result)
        }.addOnFailureListener {
            Toast.makeText(this, "Fuck!", Toast.LENGTH_LONG).show()
        }
    }

    inner class AsyncDataFetch(private val context: Context): AsyncTask<QuerySnapshot, Int, ArrayList<QueryDocumentSnapshot>>() {
        override fun doInBackground(vararg params: QuerySnapshot?): ArrayList<QueryDocumentSnapshot> {
            var list : ArrayList<QueryDocumentSnapshot> = arrayListOf()
            var result: QuerySnapshot? = params[0]
            for (doc in result!!) {
                list.add(doc)
            }
            return list
        }

        override fun onPostExecute(result: ArrayList<QueryDocumentSnapshot>?) {
            var jobsAdapter = JobsAdapter(result!!, this.context)
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
