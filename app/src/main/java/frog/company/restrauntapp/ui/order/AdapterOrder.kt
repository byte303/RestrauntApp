package frog.company.restrauntapp.ui.order

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import frog.company.restrauntapp.R
import frog.company.restrauntapp.enum.EnumDetailsStatus
import frog.company.restrauntapp.inter.IListenerProduct
import frog.company.restrauntapp.model.Product


class AdapterOrder(
    private val dataList : ArrayList<Product>,
    private val listener : IListenerProduct
) : RecyclerView.Adapter<AdapterOrder.ViewHolder>() {

    inner class ViewHolder(view : View) : RecyclerView.ViewHolder(view){
        var txtName : TextView = view.findViewById(R.id.txtName)
        var txtPrice : TextView = view.findViewById(R.id.txtPrice)
        var txtCount : TextView = view.findViewById(R.id.txtCount)
        var linear : LinearLayout = view.findViewById(R.id.linear)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.list_menu_order,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.txtName.text = String.format(dataList[position].prod_name)
        holder.txtPrice.text = String.format(dataList[position].prod_price.toString())
        holder.txtCount.text = String.format(dataList[position].prod_total.toString())

        holder.linear.setOnClickListener {
            if(dataList[position].prod_status != EnumDetailsStatus.Ready.num)
                listener.onSelectProduct(dataList[position], true)
        }
    }
}