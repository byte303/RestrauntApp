package frog.company.restrauntapp.database

import android.app.Activity
import android.util.Log
import android.widget.Toast
import frog.company.restrauntapp.inter.IListenerHall
import frog.company.restrauntapp.inter.IListenerKitchen
import frog.company.restrauntapp.inter.IListenerTable
import frog.company.restrauntapp.model.Hall
import frog.company.restrauntapp.model.Kitchen
import frog.company.restrauntapp.model.Table
import okhttp3.*
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException

class UtilsKitchen() {

    private val url = UtilsDB.mainUrl + "kitchen/"

    fun onLoadKitchen(listener : IListenerKitchen){
        val client = OkHttpClient()

        val request = Request.Builder()
            .url(url + "load_kitchen.php")
            .get()
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.e("onLoadKitchen", e.message.toString())
                listener.onResultKitchenAll(null)
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
                        var element : Kitchen
                        val array : ArrayList<Kitchen> = ArrayList()
                        for(i in 0 until obj.length()){
                            data = obj.getJSONObject(i)
                            element =
                                Kitchen(
                                    data.getInt("kitchen_id"),
                                    data.getString("kitchen_name")
                                )
                            array.add(element)
                        }
                        listener.onResultKitchenAll(array)
                    }else
                        listener.onResultKitchenAll(null)
                } catch (ex : Exception){
                    Log.e("onLoadKitchen", ex.message.toString())
                    listener.onResultKitchenAll(null)
                }
            }
        })
    }
}