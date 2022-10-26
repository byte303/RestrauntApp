package frog.company.restrauntapp.ui.order

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import frog.company.restrauntapp.R
import frog.company.restrauntapp.inter.IListenerCategory
import frog.company.restrauntapp.model.Category


class AdapterTab(
    private val dataList : ArrayList<Category>,
    private val listener : IListenerCategory
) : RecyclerView.Adapter<AdapterTab.ViewHolder>() {

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
        holder.txtNameTab.text = String.format(dataList[position].category_name)

        holder.txtNameTab.setOnClickListener {
            listener.onResultCategory(dataList[position])
        }
    }
}