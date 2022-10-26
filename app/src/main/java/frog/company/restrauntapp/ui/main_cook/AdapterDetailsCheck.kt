package frog.company.restrauntapp.ui.main_cook

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import frog.company.restrauntapp.R
import frog.company.restrauntapp.database.UtilsDetails
import frog.company.restrauntapp.enum.EnumDetailsStatus
import frog.company.restrauntapp.enum.EnumPresence
import frog.company.restrauntapp.helper.drag.OnStartDragListener
import frog.company.restrauntapp.inter.IListenerDetails
import frog.company.restrauntapp.inter.IListenerProduct
import frog.company.restrauntapp.model.OrderDetails
import frog.company.restrauntapp.model.Product


class AdapterDetailsCheck(
    private val dataList : ArrayList<OrderDetails>,
    private val listener : IListenerDetails
) : RecyclerView.Adapter<AdapterDetailsCheck.ViewHolder>() {

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
        if(dataList[position].details_comment == "")
            holder.txtTitle.text = String.format("${dataList[position].product?.prod_name!!} ${dataList[position].details_count.toInt()}шт.")
        else
            holder.txtTitle.text = String.format("${dataList[position].product?.prod_name!!} (${dataList[position].details_comment}) ${dataList[position].details_count.toInt()}шт.")

        holder.chkbox.isChecked = !(dataList[position].details_status == EnumDetailsStatus.NewOrder.num ||
                dataList[position].details_status == EnumDetailsStatus.Edit.num ||
                dataList[position].details_status == EnumDetailsStatus.Return.num)

        holder.chkbox.setOnCheckedChangeListener { _, b ->
            if(b)
                dataList[position].details_status = EnumDetailsStatus.Ready.num
            else
                dataList[position].details_status = EnumDetailsStatus.NewOrder.num

            listener.onResultDetail(dataList[position])
        }
    }
}