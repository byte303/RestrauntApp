package frog.company.restrauntapp.database

import android.app.Activity
import android.util.Log
import android.widget.Toast
import frog.company.restrauntapp.helper.Converting
import frog.company.restrauntapp.helper.UtilsEnum
import frog.company.restrauntapp.inter.IListenerOrder
import frog.company.restrauntapp.model.Hall
import frog.company.restrauntapp.model.Order
import frog.company.restrauntapp.model.Table
import frog.company.restrauntapp.model.User
import okhttp3.*
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException

class UtilsOrder() {

    private val url = UtilsDB.mainUrl + "order/"

    fun onLoadOrderUser(order_user : String, listener : IListenerOrder){
        val formBody = FormBody.Builder()
            .add("order_user", order_user)
            .build()

        val request = Request.Builder()
            .url(url + "select_user_order_phone.php")
            .get()
            .post(formBody)
            .build()

        onLoadOrder(request, listener)
    }

    fun onLoadOrderAll(listener : IListenerOrder){
        val request = Request.Builder()
            .url(url + "select_all_order_phone.php")
            .get()
            .build()

        onLoadOrder(request, listener)
    }

    fun onLoadOrderAllWaiter(listener : IListenerOrder){
        val request = Request.Builder()
            .url(url + "select_all_order_waiter_phone.php")
            .get()
            .build()

        onLoadOrder(request, listener)
    }

    private fun onLoadOrder(request : Request, listener : IListenerOrder){
        val client = OkHttpClient()


        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.e("onSelectPassword", e.message.toString())
                listener.onListOrders(null)
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
                        var order : Order
                        val arrayOrder : ArrayList<Order> = ArrayList()
                        for(i in 0 until obj.length()){
                            data = obj.getJSONObject(i)
                            order =
                                Order(
                                    data.getInt("order_id"),
                                    data.getInt("order_table"),
                                    data.getString("order_date"),
                                    data.getString("order_close_date"),
                                    data.getDouble("order_price"),
                                    data.getDouble("order_discount"),
                                    data.getInt("order_user"),
                                    data.getInt("order_shift"),
                                    data.getInt("order_payment"),
                                    data.getInt("order_status"),
                                    data.getInt("order_delivery"),
                                    data.getString("order_comment"),
                                    data.getInt("order_status_cook"),
                                    data.getInt("order_favorite"),
                                    data.getDouble("order_price_waiter"),
                                    Table(
                                        data.getInt("table_id"),
                                        data.getString("table_name"),
                                        data.getInt("table_place"),
                                        data.getInt("table_hall_id"),
                                        data.getInt("table_status"),
                                        data.getString("table_date"),
                                        data.getString("table_time"),
                                        Hall(
                                            data.getInt("hall_id"),
                                            data.getString("hall_name"),
                                            data.getDouble("hall_price")
                                        )
                                    ),
                                    User(
                                        data.getInt("user_id"),
                                        data.getString("user_name"),
                                        data.getString("user_password"),
                                        data.getInt("user_role")
                                    )
                                )
                            arrayOrder.add(order)
                        }
                        listener.onListOrders(arrayOrder)
                    }else
                        listener.onListOrders(ArrayList())
                } catch (ex : Exception){
                    Log.e("onSelectPassword", ex.message.toString())
                    listener.onListOrders(null)
                }
            }
        })
    }

    fun onInsert(orders : Order, listener : IListenerOrder){
        val client = OkHttpClient()

        val formBody = FormBody.Builder()
            .add("order_price", Converting().onConvertDouble(orders.order_price))
            .add("order_discount", Converting().onConvertDouble(orders.order_discount))
            .add("order_table", orders.order_table.toString())
            .add("order_status", orders.order_status.toString())
            .add("order_date", orders.order_date)
            .add("order_user", orders.order_user.toString())
            .add("order_shift", orders.order_shift.toString())
            .add("order_close_date", orders.order_close_date)
            .add("order_delivery", orders.order_delivery.toString())
            .add("order_comment", orders.order_comment)
            .add("order_payment", orders.order_payment.toString())
            .add("order_status_cook", orders.order_status_cook.toString())
            .add("order_price_waiter", orders.order_price_waiter.toString())
            .build()

        val request = Request.Builder()
            .url(url + "insert_order_phone.php")
            .get()
            .post(formBody)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.e("onInsert", e.message.toString())
                listener.onOrder(null)
            }

            override fun onResponse(call: Call, response: Response) {
                try{
                    var strResponse = response.body()!!.string()

                    Log.i("Information", strResponse)

                    strResponse = strResponse.replace("[[", "[")
                    strResponse = strResponse.replace("]]", "]")

                    val obj = JSONArray(strResponse)

                    if(obj.length() > 0){
                        val data = obj.getJSONObject(0)
                        val order =
                            Order(
                                data.getInt("order_id"),
                                data.getInt("order_table"),
                                data.getString("order_date"),
                                data.getString("order_close_date"),
                                data.getDouble("order_price"),
                                data.getDouble("order_discount"),
                                data.getInt("order_user"),
                                data.getInt("order_shift"),
                                data.getInt("order_payment"),
                                data.getInt("order_status"),
                                data.getInt("order_delivery"),
                                data.getString("order_comment"),
                                data.getInt("order_status_cook"),
                                data.getInt("order_favorite"),
                                data.getDouble("order_price_waiter")
                            )
                        listener.onOrder(order, UtilsEnum.ORDER_CREATE)
                    }else
                        listener.onOrder(null)
                } catch (ex : Exception){
                    Log.e("onInsert debug", ex.message.toString())
                    listener.onOrder(null)
                }
            }
        })
    }

    fun onUpdate(orders : Order, listener : IListenerOrder){
        val client = OkHttpClient()

        val formBody = FormBody.Builder()
            .add("order_price", Converting().onConvertDouble(orders.order_price))
            .add("order_discount", Converting().onConvertDouble(orders.order_discount))
            .add("order_table", orders.order_table.toString())
            .add("order_status", orders.order_status.toString())
            .add("order_shift", orders.order_shift.toString())
            .add("order_close_date", orders.order_close_date)
            .add("order_comment", orders.order_comment)
            .add("order_payment", orders.order_payment.toString())
            .add("order_status_cook", orders.order_status_cook.toString())
            .add("order_price_waiter", orders.order_price_waiter.toString())
            .add("order_id", orders.order_id.toString())
            .build()

        val request = Request.Builder()
            .url(url + "update_order.php")
            .get()
            .post(formBody)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.e("onUpdateOrder", e.message.toString())
                listener.onResultOrder(false, UtilsEnum.ORDER_DEFAULT)
            }

            override fun onResponse(call: Call, response: Response) {
                try{
                    listener.onResultOrder(true, UtilsEnum.ORDER_UPDATE)
                } catch (ex : Exception){
                    Log.e("onUpdateOrder", ex.message.toString())
                    listener.onResultOrder(false, UtilsEnum.ORDER_DEFAULT)
                }
            }
        })
    }

    fun onUpdateFavorite(orders : Order, listener : IListenerOrder){
        val client = OkHttpClient()

        val formBody = FormBody.Builder()
            .add("order_favorite", orders.order_favorite.toString())
            .add("order_id", orders.order_id.toString())
            .build()

        val request = Request.Builder()
            .url(url + "update_order_favorite.php")
            .get()
            .post(formBody)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.e("onUpdateFavorite", e.message.toString())
                listener.onResultOrder(false, UtilsEnum.ORDER_DEFAULT)
            }

            override fun onResponse(call: Call, response: Response) {
                try{
                    listener.onResultOrder(true, UtilsEnum.ORDER_UPDATE_FAVORITE)
                } catch (ex : Exception){
                    Log.e("onUpdateFavorite", ex.message.toString())
                    listener.onResultOrder(false, UtilsEnum.ORDER_DEFAULT)
                }
            }
        })
    }

    fun onUpdateStatus(orderId : Int, status : Int, listener : IListenerOrder){
        val client = OkHttpClient()

        val formBody = FormBody.Builder()
            .add("order_status", status.toString())
            .add("order_id", orderId.toString())
            .build()

        val request = Request.Builder()
            .url(url + "update_order_status.php")
            .get()
            .post(formBody)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.e("onUpdateStatus", e.message.toString())
                listener.onResultOrder(false, UtilsEnum.ORDER_DEFAULT)
            }

            override fun onResponse(call: Call, response: Response) {
                try{
                    listener.onResultOrder(true, UtilsEnum.ORDER_UPDATE_STATUS)
                } catch (ex : Exception){
                    Log.e("onUpdateStatus", ex.message.toString())
                    listener.onResultOrder(false, UtilsEnum.ORDER_DEFAULT)
                }
            }
        })
    }

    fun onUpdateTable(order : Order, order_table_after : Int, listener : IListenerOrder){
        val client = OkHttpClient()

        val formBody = FormBody.Builder()
            .add("order_table_after", order_table_after.toString())
            .add("order_table", order.order_table.toString())
            .add("order_id", order.order_id.toString())
            .build()

        val request = Request.Builder()
            .url(url + "update_order_table.php")
            .get()
            .post(formBody)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.e("onUpdateTable", e.message.toString())
                listener.onResultOrder(false, UtilsEnum.ORDER_DEFAULT)
            }

            override fun onResponse(call: Call, response: Response) {
                try{
                    listener.onResultOrder(true, UtilsEnum.ORDER_UPDATE_TABLE)
                } catch (ex : Exception){
                    Log.e("onUpdateTable", ex.message.toString())
                    listener.onResultOrder(false, UtilsEnum.ORDER_DEFAULT)
                }
            }
        })
    }

    fun onCheckTableFree(tableId : Int, listener : IListenerOrder){
        val client = OkHttpClient()

        val formBody = FormBody.Builder()
            .add("table_id", tableId.toString())
            .build()

        val request = Request.Builder()
            .url(url + "check_table_free.php")
            .get()
            .post(formBody)
            .build()


        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.e("onCheckTableFree", e.message.toString())
                listener.onResultOrder(false, UtilsEnum.ORDER_FREE_TABLE)
            }

            override fun onResponse(call: Call, response: Response) {
                try{
                    var strResponse = response.body()!!.string()

                    Log.i("Information", strResponse)

                    strResponse = strResponse.replace("[[", "[")
                    strResponse = strResponse.replace("]]", "]")

                    val obj = JSONArray(strResponse)

                    if(obj.length() > 0){
                        listener.onResultOrder(true, UtilsEnum.ORDER_FREE_TABLE)
                    }else
                        listener.onResultOrder(false, UtilsEnum.ORDER_FREE_TABLE)
                } catch (ex : Exception){
                    Log.e("onSelectPassword", ex.message.toString())
                    listener.onResultOrder(false, UtilsEnum.ORDER_FREE_TABLE)
                }
            }
        })
    }
}