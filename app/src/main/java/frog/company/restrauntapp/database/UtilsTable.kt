package frog.company.restrauntapp.database

import android.app.Activity
import android.util.Log
import android.widget.Toast
import frog.company.restrauntapp.inter.IListenerHall
import frog.company.restrauntapp.inter.IListenerTable
import frog.company.restrauntapp.model.Hall
import frog.company.restrauntapp.model.Table
import okhttp3.*
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException

class UtilsTable() {

    private val url = UtilsDB.mainUrl + "table/"

    fun onLoad(listener : IListenerTable){
        val client = OkHttpClient()

        val request = Request.Builder()
            .url(url + "load_tables.php")
            .get()
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.e("onLoadTable", e.message.toString())
                listener.onListTables(null)
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
                        var table : Table
                        val array : ArrayList<Table> = ArrayList()
                        for(i in 0 until obj.length()){
                            data = obj.getJSONObject(i)
                            table =
                                Table(
                                    data.getInt("table_id"),
                                    data.getString("table_name"),
                                    data.getInt("table_place"),
                                    data.getInt("table_hall_id"),
                                    data.getInt("table_status"),
                                    data.getString("table_date"),
                                    data.getString("table_time")
                                )
                            array.add(table)
                        }
                        listener.onListTables(array)
                    }else
                        listener.onListTables(null)
                } catch (ex : Exception){
                    Log.e("onLoadTable", ex.message.toString())
                    listener.onListTables(null)
                }
            }
        })
    }

    fun onUpdateTable(table : Table, listener : IListenerTable){
        val client = OkHttpClient()

        val formBody = FormBody.Builder()
            .add("table_time", table.table_time)
            .add("table_date", table.table_date)
            .add("table_status", table.table_status.toString())
            .add("table_id", table.table_id.toString())
            .build()

        val request = Request.Builder()
            .url(url + "update_tables_status.php")
            .get()
            .post(formBody)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.e("onUpdateTable", e.message.toString())
                listener.onResultTable(false)
            }

            override fun onResponse(call: Call, response: Response) {
                try{
                    listener.onResultTable(true)
                } catch (ex : Exception){
                    Log.e("onUpdateTable", ex.message.toString())
                    listener.onResultTable(false)
                }
            }
        })
    }
}