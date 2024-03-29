package edu.washington.lwnash45.plentifull

import android.content.Context
import android.content.Intent
import android.support.v4.content.ContextCompat.startActivity
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import com.google.firebase.Timestamp
import com.google.firebase.firestore.QueryDocumentSnapshot
import kotlin.collections.ArrayList

class JobsAdapter(private val data: ArrayList<QueryDocumentSnapshot>, private val context: Context, private val type: String):
    RecyclerView.Adapter<JobsAdapter.MyViewHolder>() {

    class MyViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val titleView: TextView = view.findViewById(R.id.jobTitle)
        val payView: TextView = view.findViewById(R.id.pay)
        val cityView: TextView = view.findViewById(R.id.city)
        val dateView: TextView = view.findViewById(R.id.date)
        val buttonView: Button = view.findViewById(R.id.addJob)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val root = LayoutInflater.from(parent.context).inflate(R.layout.job, parent, false)
        return MyViewHolder(root)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        var dataPosition = data[position].data

        holder.titleView.text = dataPosition["job_header"].toString()
        holder.payView.text = "$" + dataPosition["hourly_wage_to_admin"].toString() + " /Hr"
        holder.cityView.text = "Location: " + dataPosition["location"].toString()

        var date = dataPosition["from"] as Timestamp
        holder.dateView.text = "Date: " + date.toDate().toString()

        when (this.type) {
            "NEW" -> {
                holder.buttonView.setOnClickListener {
                    val intent = Intent(this.context, AddJobActivity::class.java)
                    intent.putExtra("ID", data[position].id)
                    startActivity(this.context, intent, null)
                }
            }
            else -> holder.buttonView.visibility = View.INVISIBLE
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }
}