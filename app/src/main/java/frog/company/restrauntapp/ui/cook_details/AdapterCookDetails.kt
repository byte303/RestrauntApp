package frog.company.restrauntapp.ui.cook_details

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import frog.company.restrauntapp.R
import frog.company.restrauntapp.enum.EnumDetailsStatus
import frog.company.restrauntapp.enum.EnumOrderStatus
import frog.company.restrauntapp.helper.UtilsEnum
import frog.company.restrauntapp.inter.IListenerDetails
import frog.company.restrauntapp.inter.IListenerOrder
import frog.company.restrauntapp.model.Order
import frog.company.restrauntapp.model.OrderDetails


class AdapterCookDetails(
    private val dataList : ArrayList<OrderDetails>,
    private val listener : IListenerDetails
) : RecyclerView.Adapter<AdapterCookDetails.ViewHolder>() {

    private lateinit var arrayColor : IntArray
    private lateinit var arrayTextStatus : Array<String>

    inner class ViewHolder(view : View) : RecyclerView.ViewHolder(view){
        var txtTitle : TextView = view.findViewById(R.id.txtTitle)
        var txtStatus : TextView = view.findViewById(R.id.txtStatus)
        var linear : ConstraintLayout = view.findViewById(R.id.linear)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        arrayColor = parent.context.resources.getIntArray(R.array.order_cook_status_color)
        arrayTextStatus = parent.context.resources.getStringArray(R.array.order_cook_status)

        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.list_details_cook,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.txtTitle.text = String.format("${dataList[position].product!!.prod_name}\nКоличество: ${dataList[position].details_count}")

        holder.txtStatus.text = arrayTextStatus[dataList[position].details_status]
        holder.txtStatus.setTextColor(arrayColor[dataList[position].details_status])

        holder.linear.setOnClickListener {
            listener.onResultDetail(dataList[position])
        }
    }
}