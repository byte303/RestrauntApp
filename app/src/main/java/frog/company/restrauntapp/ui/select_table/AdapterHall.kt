package frog.company.restrauntapp.ui.select_table

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import frog.company.restrauntapp.R
import frog.company.restrauntapp.enum.EnumOrderStatus
import frog.company.restrauntapp.inter.IListenerCategory
import frog.company.restrauntapp.inter.IListenerHall
import frog.company.restrauntapp.inter.IListenerOrder
import frog.company.restrauntapp.model.Category
import frog.company.restrauntapp.model.Hall
import frog.company.restrauntapp.model.Order


class AdapterHall(
    private val dataList : ArrayList<Hall>,
    private val listener : IListenerHall
) : RecyclerView.Adapter<AdapterHall.ViewHolder>() {

    inner class ViewHolder(view : View) : RecyclerView.ViewHolder(view){
        var txtNameTab : TextView = view.findViewById(R.id.txtNameTab)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.list_tab,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.txtNameTab.text = String.format(dataList[position].hall_name)

        holder.txtNameTab.setOnClickListener {
            listener.onSelectHall(dataList[position])
        }
    }
}