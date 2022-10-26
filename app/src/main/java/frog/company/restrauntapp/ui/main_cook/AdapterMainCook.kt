package frog.company.restrauntapp.ui.main_cook

import android.app.Activity
import android.content.Context
import android.os.Build
import android.telecom.Call
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import frog.company.restrauntapp.R
import frog.company.restrauntapp.database.UtilsDetails
import frog.company.restrauntapp.enum.EnumOrderStatus
import frog.company.restrauntapp.helper.UtilsEnum
import frog.company.restrauntapp.inter.IListenerDetails
import frog.company.restrauntapp.inter.IListenerOrder
import frog.company.restrauntapp.model.Order
import frog.company.restrauntapp.model.OrderDetails
import org.w3c.dom.Text
import java.sql.Time
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.Period
import java.util.*
import kotlin.collections.ArrayList


class AdapterMainCook(
    private val activity : Activity,
    private val dataList : ArrayList<Order>,
    private val listener : IListenerOrder
) : RecyclerView.Adapter<AdapterMainCook.ViewHolder>(), IListenerDetails {

    private lateinit var context : Context

    private lateinit var arrayColor : IntArray
    private val formatFull = SimpleDateFormat("dd.MM.yyyy HH:mm")
    private val format = SimpleDateFormat("HH:mm")
    private lateinit var date : Date

    private var timer: Timer? = null
    private var tTask: TimerTask? = null
    private var interval: Long = 1000

    private var dateNow = Date()
    private lateinit var time1 : Time
    private lateinit var time2 : Time
    private lateinit var _date : Date

    inner class ViewHolder(view : View) : RecyclerView.ViewHolder(view){
        var txtNumber : TextView = view.findViewById(R.id.txtNumber)
        var txtName : TextView = view.findViewById(R.id.txtName)
        var txtTable : TextView = view.findViewById(R.id.txtTable)
        var txtTimer : TextView = view.findViewById(R.id.txtTimer)
        var listDetails : RecyclerView = view.findViewById(R.id.listDetails)
        var linear : CardView = view.findViewById(R.id.linear)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        context = parent.context

        arrayColor = context.resources.getIntArray(R.array.order_cook_status_color)

        timer = Timer()
        schedule()

        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.list_order_cook,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        date = formatFull.parse(dataList[position].order_date)!!
        holder.txtTimer.text = format.format(date)
        ////////////////
        dateNow = Date()
        time1 = Time(date.hours, date.minutes, date.seconds)
        time2 = Time(dateNow.hours, dateNow.minutes, dateNow.seconds)

        _date = difference(time2, time1)
        holder.txtTimer.text = "${_date.hours}:${_date.minutes}:${_date.seconds}"


        holder.listDetails.adapter = AdapterDetailsCheck(dataList[position].orderDetails!!, this)

        holder.txtNumber.text = String.format("№${dataList[position].order_id}")
        holder.txtName.text = dataList[position].user?.user_name
        holder.txtTable.text = dataList[position].table?.table_name
        holder.linear.setCardBackgroundColor(arrayColor[dataList[position].order_status_cook])

        if(dataList[position].order_favorite == 1)
            holder.linear.setCardBackgroundColor(context.resources.getColor(R.color.blue))

        holder.linear.setOnClickListener {
            listener.onOrder(dataList[position], UtilsEnum.ORDER_SELECT)
        }
        holder.linear.setOnLongClickListener {
            listener.onOrder(dataList[position], UtilsEnum.ORDER_FAVORITE)
            true
        }
    }

    private fun schedule() {
        tTask?.cancel()
        if (interval > 0) {
            tTask = object : TimerTask() {
                override fun run() {
                    activity.runOnUiThread {
                        notifyDataSetChanged()
                    }
                }
            }
            timer?.schedule(tTask, 1000, interval)
        }
    }

    private fun difference(start: Time, stop: Time): Time {
        val diff = Time(0, 0, 0)
        if (stop.seconds > start.seconds) {
            --start.minutes
            start.seconds += 60
        }
        diff.seconds = start.seconds - stop.seconds
        if (stop.minutes > start.minutes) {
            --start.hours
            start.minutes += 60
        }
        diff.minutes = start.minutes - stop.minutes
        diff.hours = start.hours - stop.hours
        return diff
    }

    override fun onBooleanResult(result: Int) {

    }

    override fun onIndexResult(result: Int) {

    }

    override fun onArrayDetails(result: ArrayList<OrderDetails>?) {

    }

    override fun onResultDetail(result: OrderDetails?) {
        if(result != null){
            val array = arrayListOf(result)

            Log.i("UPDATE_OrderDetails", "${result.details_id} AND ${result.details_id}")

            UtilsDetails().onUpdate(array, this)
        }
    }
}