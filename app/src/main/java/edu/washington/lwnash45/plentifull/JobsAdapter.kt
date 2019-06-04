package edu.washington.lwnash45.plentifull

import android.content.Context
import android.support.constraint.ConstraintLayout
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import org.w3c.dom.Text


//class JobsAdapter(context: Context, private val data: ArrayList<Map<String, Any>>) : ArrayAdapter<Any>(context, R.layout.job, data as List<*>) {
//
//    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
//        var root = LayoutInflater.from(this.context).inflate(R.layout.job, parent, false)
//        val title = root.findViewById<TextView>(R.id.jobTitle)
//        Log.d("Yo", this.data[position]["client_name"].toString())
//        title.text = this.data[position]["client_name"].toString()
//        return root
//    }
//}

class JobsAdapter(private val data: ArrayList<Map<String, Any>>) : RecyclerView.Adapter<JobsAdapter.MyViewHolder>() {

    class MyViewHolder(val view: View): RecyclerView.ViewHolder(view) {
        val textView = view.findViewById<TextView>(R.id.jobTitle)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val root = LayoutInflater.from(parent.context).inflate(R.layout.job, parent, false)
        return MyViewHolder(root)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        Log.d("Yo", data[position]["client_name"].toString())
        holder.textView.text = data[position]["client_name"].toString()

    }

    override fun getItemCount(): Int {
        return data.size
    }
}