package com.example.dailiesandroidapp
import com.example.dailiesandroidapp.DbManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
//This is the adapter class. It binds data from Model and connects it to the recyclerview. Also handles deletion.
class myAdapter(dataholder: ArrayList<Model>) : RecyclerView.Adapter<myAdapter.MyViewHolder>() {
    var dataholder: ArrayList<Model> = ArrayList() //Holding Dailies in Datastructure ArrayList

    init {
        this.dataholder = dataholder
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(
            R.layout.single_reminder_file,
            parent,
            false
        )

        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = dataholder[position]
        holder.mTitle.text = currentItem.title ?: "No Title"
        holder.mDate.text = currentItem.date ?: "No Date"
        holder.mTime.text = currentItem.time ?: "No Time"


        holder.delBtn.setOnClickListener {

            dataholder.removeAt(position)
            // Notify the adapter that the data set has changed and removes from DB
            notifyDataSetChanged()
            val titleToDelete = currentItem.title.toString()
            val dbManager = DbManager(holder.itemView.context)
            val rowsAffected = dbManager.deleteremindertitle(titleToDelete)
        }
    }

    override fun getItemCount(): Int {
        return dataholder.size
    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var mTitle: TextView = itemView.findViewById(R.id.txtTitle) as TextView
        var mDate: TextView = itemView.findViewById(R.id.txtDate) as TextView
        var mTime: TextView = itemView.findViewById(R.id.txtTime) as TextView
        var delBtn: ImageButton = itemView.findViewById(R.id.delBtn) as ImageButton
    }
}
