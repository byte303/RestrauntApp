package frog.company.restrauntapp

import android.app.Service
import android.content.Intent
import android.media.MediaPlayer
import android.os.Binder
import android.os.IBinder
import android.util.Log
import frog.company.restrauntapp.database.UtilsDB
import frog.company.restrauntapp.database.UtilsOrder
import frog.company.restrauntapp.inter.IListenerOrder
import frog.company.restrauntapp.model.Order
import java.util.*

class MyService : Service(), IListenerOrder {
    private val LOG_TAG = "myLogs"
    private var binder: MyBinder = MyBinder()
    private var timer: Timer? = null
    private var tTask: TimerTask? = null
    private var interval: Long = 1000

    private var array : ArrayList<Order>? = null

    override fun onCreate() {
        super.onCreate()
        Log.d(LOG_TAG, "MyService onCreate")
        timer = Timer()
        schedule()
    }

    private fun schedule() {
        tTask?.cancel()
        if (interval > 0) {
            tTask = object : TimerTask() {
                override fun run() {
                    UtilsOrder().onLoadOrderAll(this@MyService)
                }
            }
            timer?.schedule(tTask, 1000, interval)
        }
    }

    override fun onBind(arg0: Intent): IBinder {
        Log.d(LOG_TAG, "MyService onBind")
        return binder
    }

    inner class MyBinder : Binder() {
        val service: MyService
            get() = this@MyService
    }

    override fun onListOrders(result: ArrayList<Order>?) {
        array = if(array == null){
            result
        }else{
            if(array!!.size < result!!.size) {
                Log.d(LOG_TAG, "Сработало уведомление")
                val mediaPlayer = MediaPlayer.create(UtilsDB.MY_ACTIVITY, R.raw.new_order)
                mediaPlayer.start()
                result
            }else
                result
        }
    }

    override fun onOrder(result: Order?, index: Int) {

    }

    override fun onResultOrder(boolean: Boolean, result: Int) {

    }
}