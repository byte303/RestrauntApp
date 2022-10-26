package frog.company.restrauntapp.database

import android.app.Activity
import android.util.Log
import android.widget.Toast
import frog.company.restrauntapp.model.Cashe
import io.paperdb.Paper
import okhttp3.*
import org.json.JSONArray
import java.io.IOException

class UtilsCashe() {

    private val url = UtilsDB.mainUrl + "cashe/"

    fun onLoadCashe(){
        val client = OkHttpClient()

        val request = Request.Builder()
            .url(url + "load_cashe.php")
            .get()
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.e("onLoadCashe", e.message.toString())
            }

            override fun onResponse(call: Call, response: Response) {
                try{
                    var strResponse = response.body()!!.string()

                    Log.i("Information", strResponse)

                    strResponse = strResponse.replace("[[", "[")
                    strResponse = strResponse.replace("]]", "]")

                    val obj = JSONArray(strResponse)

                    val data = obj.getJSONObject(0)
                    val element =
                        Cashe(
                            data.getDouble("cashe_card"),
                            data.getDouble("cashe_money"),
                            data.getDouble("cashe_percent"),
                            data.getDouble("cashe_fix"),
                            data.getInt("cashe_type")
                        )
                    Paper.book().write(UtilsDB.CASHE, element)
                } catch (ex : Exception){
                    Log.e("onLoadCashe", ex.message.toString())
                }
            }
        })
    }
}