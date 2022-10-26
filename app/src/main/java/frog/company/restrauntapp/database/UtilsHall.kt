package frog.company.restrauntapp.database

import android.app.Activity
import android.util.Log
import android.widget.Toast
import frog.company.restrauntapp.inter.IListenerHall
import frog.company.restrauntapp.inter.IListenerOrder
import frog.company.restrauntapp.model.Hall
import frog.company.restrauntapp.model.Order
import frog.company.restrauntapp.model.Table
import okhttp3.*
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException

class UtilsHall() {

    private val url = UtilsDB.mainUrl + "hall/"

    fun onLoadHall(listener : IListenerHall){
        val client = OkHttpClient()

        val request = Request.Builder()
            .url(url + "load_hall.php")
            .get()
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.e("onLoadHall", e.message.toString())
                listener.onListHalls(null)
            }

            override fun onResponse(call: Call, response: Response) {
                try{
                    var strResponse = response.body()!!.string()

                    Log.i("Information", strResponse)

                    strResponse = strResponse.replace("[[", "[")
                    strResponse = strResponse.replace("]]", "]")

                    val obj = JSONArray(strResponse)

                    if(obj.length() > 0){
                        var data : JSONObject
                        var hall : Hall
                        val arrayHall : ArrayList<Hall> = ArrayList()
                        for(i in 0 until obj.length()){
                            data = obj.getJSONObject(i)
                            hall =
                                Hall(
                                    data.getInt("hall_id"),
                                    data.getString("hall_name"),
                                    data.getDouble("hall_price")
                                )
                            arrayHall.add(hall)
                        }
                        listener.onListHalls(arrayHall)
                    }else
                        listener.onListHalls(null)
                } catch (ex : Exception){
                    Log.e("onLoadHall", ex.message.toString())
                    listener.onListHalls(null)
                }
            }
        })
    }
}