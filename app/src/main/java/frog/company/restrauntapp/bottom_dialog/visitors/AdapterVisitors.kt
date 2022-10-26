package frog.company.restrauntapp.bottom_dialog.visitors

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import frog.company.restrauntapp.R

class AdapterVisitors(
    private val listener : IListenerVisitors
) : RecyclerView.Adapter<AdapterVisitors.ViewHolder>() {

    inner class ViewHolder(view : View) : RecyclerView.ViewHolder(view){
        var txtTitle : TextView = view.findViewById(R.id.txtTitle)
        var linear : LinearLayout = view.findViewById(R.id.linear)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.list_visitors,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return 6
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.txtTitle.text = String.format("${position+1}")

        holder.linear.setOnClickListener {
            listener.onResultVisitors(position+1)
        }
    }
}