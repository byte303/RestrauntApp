package frog.company.restrauntapp.database

import android.app.Activity
import android.util.Log
import android.widget.Toast
import frog.company.restrauntapp.inter.IListenerShift
import frog.company.restrauntapp.inter.IListenerUser
import frog.company.restrauntapp.model.Shift
import frog.company.restrauntapp.model.User
import okhttp3.*
import org.json.JSONArray
import java.io.IOException

class UtilsShift() {

    private val url = UtilsDB.mainUrl + "shift/"

    fun onSelectLast(userId : Int, listener : IListenerShift){
        val client = OkHttpClient()

        val formBody = FormBody.Builder()
            .add("shift_user_id", userId.toString())
            .build()

        val request = Request.Builder()
            .url(url + "select_user_shift.php")
            .get()
            .post(formBody)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.e("onSelectLast", e.message.toString())
                listener.onSelectShift(null)
            }

            override fun onResponse(call: Call, response: Response) {
                try{
                    var strResponse = response.body()!!.string()

                    strResponse = strResponse.replace("[[", "[")
                    strResponse = strResponse.replace("]]", "]")

                    val obj = JSONArray(strResponse)

                    if(obj.length() > 0){
                        val data = obj.getJSONObject(0)
                        val shift =
                            Shift(
                                data.getInt("shift_id"),
                                data.getString("shift_date_open"),
                                data.getString("shift_date_close"),
                                data.getInt("shift_user_id"),
                                data.getInt("shift_status")
                            )
                        listener.onSelectShift(shift)
                    }else
                        listener.onSelectShift(null)

                } catch (ex : Exception){
                    Log.e("onSelectLast", ex.message.toString())
                    listener.onSelectShift(null)
                }
            }
        })
    }

    fun onInsert(shiftMy : Shift, listener : IListenerShift){
        val client = OkHttpClient()

        val formBody = FormBody.Builder()
            .add("shift_date_open", shiftMy.shift_date_open)
            .add("shift_date_close", shiftMy.shift_date_close)
            .add("shift_user_id", shiftMy.shift_user_id.toString())
            .add("shift_status", shiftMy.shift_status.toString())
            .build()

        val request = Request.Builder()
            .url(url + "insert_shift_phone.php")
            .get()
            .post(formBody)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.e("onInsertShift", e.message.toString())
                listener.onSelectShift(null)
            }

            override fun onResponse(call: Call, response: Response) {
                try{
                    var strResponse = response.body()!!.string()

                    strResponse = strResponse.replace("[[", "[")
                    strResponse = strResponse.replace("]]", "]")

                    val obj = JSONArray(strResponse)

                    if(obj.length() > 0){
                        val data = obj.getJSONObject(0)
                        val shift =
                            Shift(
                                data.getInt("shift_id"),
                                data.getString("shift_date_open"),
                                data.getString("shift_date_close"),
                                data.getInt("shift_user_id"),
                                data.getInt("shift_status")
                            )
                        listener.onSelectShift(shift)
                    }else
                        listener.onSelectShift(null)

                } catch (ex : Exception){
                    Log.e("onInsertShift", ex.message.toString())
                    listener.onSelectShift(null)
                }
            }
        })
    }
}