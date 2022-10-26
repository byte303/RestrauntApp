package frog.company.restrauntapp.database

import android.app.Activity
import android.util.Log
import android.widget.Toast
import frog.company.restrauntapp.database.UtilsDB.mainUrl
import frog.company.restrauntapp.inter.IListenerUser
import frog.company.restrauntapp.model.Category
import frog.company.restrauntapp.model.Kitchen
import frog.company.restrauntapp.model.User
import okhttp3.*
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException

class UtilsUser() {

    private val url = mainUrl + "user/"

    fun onSelectPassword(password : String, listener : IListenerUser){
        val client = OkHttpClient()

        val formBody = FormBody.Builder()
            .add("user_password", password)
            .build()

        val request = Request.Builder()
            .url(url + "select_password_user_phone.php")
            .get()
            .post(formBody)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.e("onSelectPassword", e.message.toString())
                listener.onSelectUser(null)
            }

            override fun onResponse(call: Call, response: Response) {
                try{
                    var strResponse = response.body()!!.string()

                    strResponse = strResponse.replace("[[", "[")
                    strResponse = strResponse.replace("]]", "]")

                    val obj = JSONArray(strResponse)

                    if(obj.length() > 0){
                        val data = obj.getJSONObject(0)
                        val user =
                            User(
                                data.getInt("user_id"),
                                data.getString("user_name"),
                                data.getString("user_password"),
                                data.getInt("user_role"),
                                data.getInt("user_kitchen"),
                                null,
                                Kitchen(
                                    data.getInt("kitchen_id"),
                                    data.getString("kitchen_name")
                                )
                            )
                        listener.onSelectUser(user)
                    }else
                        listener.onSelectUser(null)

                } catch (ex : Exception){
                    Log.e("onSelectPassword", ex.message.toString())
                    listener.onSelectUser(null)
                }
            }
        })
    }

    fun onLoad(listener : IListenerUser){
        val client = OkHttpClient()

        val request = Request.Builder()
            .url(url + "all_user.php")
            .get()
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.e("onLoad", e.message.toString())
                listener.onSelectAllUser(null)
            }

            override fun onResponse(call: Call, response: Response) {
                try{
                    var strResponse = response.body()!!.string()

                    strResponse = strResponse.replace("[[", "[")
                    strResponse = strResponse.replace("]]", "]")

                    val obj = JSONArray(strResponse)

                    if(obj.length() > 0){
                        var data : JSONObject
                        var user : User
                        val array : ArrayList<User> = ArrayList()
                        for(i in 0 until obj.length()){
                            data = obj.getJSONObject(i)
                            user =
                                User(
                                    data.getInt("user_id"),
                                    data.getString("user_name"),
                                    data.getString("user_password"),
                                    data.getInt("user_role"),
                                    data.getInt("user_kitchen"),
                                    null,
                                    null
                                )
                            array.add(user)
                        }
                        listener.onSelectAllUser(array)
                    }else
                        listener.onSelectAllUser(null)
                } catch (ex : Exception){
                    Log.e("onLoad", ex.message.toString())
                    listener.onSelectAllUser(null)
                }
            }
        })
    }

    fun onUpdateUser(user : User, listener : IListenerUser){
        val client = OkHttpClient()

        val formBody = FormBody.Builder()
            .add("user_name", user.user_name)
            .add("user_password", user.user_password)
            .add("user_role", user.user_role.toString())
            .add("user_id", user.user_id.toString())
            .build()

        val request = Request.Builder()
            .url(url + "update_user.php")
            .get()
            .post(formBody)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.e("onUpdateUser", e.message.toString())
                listener.onUserBooleanResult(false)
            }

            override fun onResponse(call: Call, response: Response) {
                try{
                    listener.onUserBooleanResult(true)
                } catch (ex : Exception){
                    Log.e("onUpdateUser", ex.message.toString())
                    listener.onUserBooleanResult(false)
                }
            }
        })
    }
}