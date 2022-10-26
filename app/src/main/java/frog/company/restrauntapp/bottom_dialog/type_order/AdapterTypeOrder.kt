package frog.company.restrauntapp.bottom_dialog.type_order

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import frog.company.restrauntapp.R

class AdapterTypeOrder(
    private val arrayText : ArrayList<String>,
    private val arrayDrawable : ArrayList<Int>,
    private val listener : IListenerTypeOrder
) : RecyclerView.Adapter<AdapterTypeOrder.ViewHolder>() {

    inner class ViewHolder(view : View) : RecyclerView.ViewHolder(view){
        var txtTitle : TextView = view.findViewById(R.id.txtTitle)
        var imgTitle : ImageView = view.findViewById(R.id.imgTitle)
        var linear : LinearLayout = view.findViewById(R.id.linear)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.list_type_order,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return arrayText.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.txtTitle.text = arrayText[position]
        holder.imgTitle.setImageResource(arrayDrawable[position])

        holder.linear.setOnClickListener {
            listener.onResultType(position)
        }
    }
}