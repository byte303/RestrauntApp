package frog.company.restrauntapp.ui.order

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import frog.company.restrauntapp.R
import frog.company.restrauntapp.helper.drag.OnStartDragListener
import frog.company.restrauntapp.inter.IListenerProduct
import frog.company.restrauntapp.model.Product


class AdapterProduct(
    private val dataList : ArrayList<Product>,
    private val listener : IListenerProduct
) : RecyclerView.Adapter<AdapterProduct.ViewHolder>() {

    inner class ViewHolder(view : View) : RecyclerView.ViewHolder(view){

        var txtName : TextView = view.findViewById(R.id.txtName)
        var txtPrice : TextView = view.findViewById(R.id.txtPrice)
        var txtValue : TextView = view.findViewById(R.id.txtValue)
        var txtType : TextView = view.findViewById(R.id.txtType)
        var linear : LinearLayout = view.findViewById(R.id.linear)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.list_product,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.txtName.text = String.format("${dataList[position].prod_name} (${dataList[position].prod_total})")
        holder.txtPrice.text = String.format(dataList[position].prod_price.toString())
        holder.txtValue.text = String.format(dataList[position].prod_count.toString())
        holder.txtType.text = String.format(dataList[position].type!!.type_name)

        holder.linear.setOnClickListener {
            dataList[position].prod_total++
            notifyItemChanged(position)

            val el: Product = dataList[position]
            listener.onSelectProduct(el)
        }
    }
}