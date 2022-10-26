package frog.company.restrauntapp.ui.home

import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import frog.company.restrauntapp.R
import frog.company.restrauntapp.enum.EnumDetailsStatus
import frog.company.restrauntapp.enum.EnumOrderStatus
import frog.company.restrauntapp.helper.UtilsEnum
import frog.company.restrauntapp.inter.IListenerOrder
import frog.company.restrauntapp.model.Order


class AdapterHome(
    private val dataList : ArrayList<Order>,
    private val listener : IListenerOrder
) : RecyclerView.Adapter<AdapterHome.ViewHolder>() {

    private lateinit var context : Context
    private var colors : Int =0

    inner class ViewHolder(view : View) : RecyclerView.ViewHolder(view){
        var txtNumber : TextView = view.findViewById(R.id.txtNumber)
        var txtHall : TextView = view.findViewById(R.id.txtHall)
        var txtDate : TextView = view.findViewById(R.id.txtDate)
        var txtPrice : TextView = view.findViewById(R.id.txtPrice)
        var txtStatus : TextView = view.findViewById(R.id.txtStatus)
        var txtComment : TextView = view.findViewById(R.id.txtComment)
        var linear : CardView = view.findViewById(R.id.linear)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        context = parent.context

        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.list_order,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.txtNumber.text = String.format("Заказ №${dataList[position].order_id}")
        holder.txtHall.text = String.format("${dataList[position].table!!.hall!!.hall_name}, ${dataList[position].table!!.table_name}")
        holder.txtDate.text = String.format("${dataList[position].order_date} - ${dataList[position].onCloseDate()}")
        holder.txtPrice.text = String.format("${dataList[position].order_price}")
        holder.txtStatus.text = when(dataList[position].order_status_cook){
            EnumDetailsStatus.NewOrder.num -> "На готовке"
            EnumDetailsStatus.Edit.num -> "На готовке"
            EnumDetailsStatus.Return.num -> "На готовке"
            EnumDetailsStatus.Ready.num -> "Подано"
            else -> "Отмена"
        }

        holder.txtComment.text = String.format(dataList[position].order_comment)

        colors = when(dataList[position].order_status_cook) {
            EnumDetailsStatus.NewOrder.num -> context.resources.getColor(R.color.orange)
            EnumDetailsStatus.Edit.num -> context.resources.getColor(R.color.orange)
            EnumDetailsStatus.Return.num -> context.resources.getColor(R.color.orange)
            EnumDetailsStatus.Ready.num -> context.resources.getColor(R.color.green)
            else -> context.resources.getColor(R.color.red)
        }
        holder.linear.setCardBackgroundColor(colors)

        holder.linear.setOnClickListener {
            listener.onOrder(dataList[position])
        }
    }
}