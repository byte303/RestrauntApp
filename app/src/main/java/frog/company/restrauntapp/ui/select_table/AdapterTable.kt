package frog.company.restrauntapp.ui.select_table

import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import frog.company.restrauntapp.R
import frog.company.restrauntapp.enum.EnumOrderStatus
import frog.company.restrauntapp.enum.EnumTablesStatus
import frog.company.restrauntapp.inter.IListenerCategory
import frog.company.restrauntapp.inter.IListenerHall
import frog.company.restrauntapp.inter.IListenerOrder
import frog.company.restrauntapp.inter.IListenerTable
import frog.company.restrauntapp.model.Category
import frog.company.restrauntapp.model.Hall
import frog.company.restrauntapp.model.Order
import frog.company.restrauntapp.model.Table


class AdapterTable(
    private val dataList : ArrayList<Table>,
    private val listener : IListenerTable
) : RecyclerView.Adapter<AdapterTable.ViewHolder>() {

    private lateinit var context : Context

    inner class ViewHolder(view : View) : RecyclerView.ViewHolder(view){
        var linear : LinearLayout = view.findViewById(R.id.linear)
        var txtName : TextView = view.findViewById(R.id.txtName)
        var txtStatus : TextView = view.findViewById(R.id.txtStatus)
        var cardView : CardView = view.findViewById(R.id.cardView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        context = parent.context

        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.list_table,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.txtName.text = String.format(dataList[position].table_name)
        holder.txtStatus.text = context.resources.getStringArray(R.array.table_status)[dataList[position].table_status]

        when(dataList[position].table_status){
            EnumTablesStatus.Free.num -> holder.cardView.setCardBackgroundColor(context.getColor(R.color.green))
            EnumTablesStatus.Busy.num -> holder.cardView.setCardBackgroundColor(context.getColor(R.color.red))
            EnumTablesStatus.Booked.num -> holder.cardView.setCardBackgroundColor(context.getColor(R.color.yellow))
        }

        holder.linear.setOnClickListener {
            listener.onSelectTable(dataList[position])
        }
    }
}