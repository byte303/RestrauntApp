package frog.company.restrauntapp.ui.cook_list_menu

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import frog.company.restrauntapp.R
import frog.company.restrauntapp.enum.EnumDetailsStatus
import frog.company.restrauntapp.enum.EnumPresence
import frog.company.restrauntapp.helper.drag.OnStartDragListener
import frog.company.restrauntapp.inter.IListenerDetails
import frog.company.restrauntapp.inter.IListenerProduct
import frog.company.restrauntapp.model.OrderDetails
import frog.company.restrauntapp.model.Product


class AdapterCookProduct(
    private val dataList : ArrayList<Product>,
    private val listener : IListenerProduct
) : RecyclerView.Adapter<AdapterCookProduct.ViewHolder>() {

    inner class ViewHolder(view : View) : RecyclerView.ViewHolder(view){
        var txtTitle : TextView = view.findViewById(R.id.txtTitle)
        var chkbox : CheckBox = view.findViewById(R.id.chkbox)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.list_product_check,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.txtTitle.text = String.format(dataList[position].prod_name)
        holder.chkbox.isChecked = dataList[position].prod_presence == EnumPresence.Yes.num
        holder.chkbox.setOnCheckedChangeListener { _, _ ->
            listener.onIndexResult(position)
        }
    }
}