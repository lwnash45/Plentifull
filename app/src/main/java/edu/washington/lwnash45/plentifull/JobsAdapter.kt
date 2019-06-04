package edu.washington.lwnash45.plentifull

import android.content.Context
import android.content.Intent
import android.support.constraint.ConstraintLayout
import android.support.v4.content.ContextCompat.startActivity
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.TextView
import org.w3c.dom.Text
import com.google.firebase.Timestamp
import com.google.firebase.firestore.QueryDocumentSnapshot
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class JobsAdapter(private val data: ArrayList<QueryDocumentSnapshot>, private val context: Context) : RecyclerView.Adapter<JobsAdapter.MyViewHolder>() {

    class MyViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val titletView = view.findViewById<TextView>(R.id.jobTitle)
        val payView = view.findViewById<TextView>(R.id.pay)
        val cityView = view.findViewById<TextView>(R.id.city)
        val dateView = view.findViewById<TextView>(R.id.date)
        val buttonView = view.findViewById<Button>(R.id.addJob)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val root = LayoutInflater.from(parent.context).inflate(R.layout.job, parent, false)
        return MyViewHolder(root)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        var dataPosition = data[position].data

        holder.titletView.text = dataPosition["job_header"].toString()
        holder.payView.text = "$" + dataPosition["hourly_wage_to_admin"].toString() + " /Hr"
        holder.cityView.text = "Location: " + dataPosition["location"].toString()

        var date = dataPosition["from"] as Timestamp
        holder.dateView.text = "Date: " + date.toDate().toString()

        holder.buttonView.setOnClickListener {
            val intent = Intent(this.context, AddJobActivity::class.java)
            intent.putExtra("ID", data[position].id)
            startActivity(this.context, intent, null)
        }

    }

    override fun getItemCount(): Int {
        return data.size
    }


}