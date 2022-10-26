package frog.company.restrauntapp.ui.main_cook

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import frog.company.restrauntapp.R
import frog.company.restrauntapp.inter.IListenerCategory
import frog.company.restrauntapp.inter.IListenerKitchen
import frog.company.restrauntapp.model.Category
import frog.company.restrauntapp.model.Kitchen


class AdapterKitchen(
    private val dataList : ArrayList<Kitchen>,
    private val listener : IListenerKitchen
) : RecyclerView.Adapter<AdapterKitchen.ViewHolder>() {

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
        holder.txtNameTab.text = String.format(dataList[position].kitchen_name)

        holder.txtNameTab.setOnClickListener {
            listener.onResultKitchen(dataList[position])
        }
    }
}