package frog.company.restrauntapp.database

import android.app.Activity
import android.util.Log
import android.widget.Toast
import frog.company.restrauntapp.helper.UtilsEnum
import frog.company.restrauntapp.inter.IListenerDetails
import frog.company.restrauntapp.inter.IListenerOrder
import frog.company.restrauntapp.model.*
import okhttp3.*
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException

class UtilsDetails() {

    private val url = UtilsDB.mainUrl + "details/"

    fun onDeleteDetails(detailsOrder : String, listener : IListenerDetails){
        val client = OkHttpClient()

        val formBody = FormBody.Builder()
            .add("details_order", detailsOrder)
            .build()

        val request = Request.Builder()
            .url(url + "delete_order_details.php")
            .get()
            .post(formBody)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.e("onDeleteDetails", e.message.toString())
                listener.onBooleanResult(UtilsEnum.DETAILS_DEFAULT)
            }

            override fun onResponse(call: Call, response: Response) {
                try{
                    listener.onBooleanResult(UtilsEnum.DETAILS_DELETE)
                } catch (ex : Exception){
                    Log.e("onDeleteDetails", ex.message.toString())
                    listener.onBooleanResult(UtilsEnum.DETAILS_DEFAULT)
                }
            }
        })
    }

    fun onInsert(arrayDetails : ArrayList<OrderDetails>, listener : IListenerDetails){
        val client = OkHttpClient()
        var formBody : FormBody
        var request : Request

        for((index, i) in arrayDetails.withIndex()){
            formBody = FormBody.Builder()
                .add("details_count", i.details_count.toString())
                .add("details_prod", i.details_prod.toString())
                .add("details_order", i.details_order.toString())
                .add("details_status", i.details_status.toString())
                .add("details_comment", i.details_comment)
                .build()

            request = Request.Builder()
                .url(url + "insert_order_details.php")
                .get()
                .post(formBody)
                .build()

            client.newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    Log.e("onInsertDetails", e.message.toString())
                    listener.onBooleanResult(UtilsEnum.DETAILS_DEFAULT)
                }

                override fun onResponse(call: Call, response: Response) {
                    try{
                        if(index == arrayDetails.size-1) {
                            listener.onBooleanResult(UtilsEnum.DETAILS_INSERT)
                        }
                    } catch (ex : Exception){
                        Log.e("onInsertDetails", ex.message.toString())
                        listener.onBooleanResult(UtilsEnum.DETAILS_DEFAULT)
                    }
                }
            })
        }
    }

    fun onUpdate(arrayDetails : ArrayList<OrderDetails>, listener : IListenerDetails){
        val client = OkHttpClient()
        var formBody : FormBody
        var request : Request

        for((index, i) in arrayDetails.withIndex()){
            formBody = FormBody.Builder()
                .add("details_status", i.details_status.toString())
                .add("details_id", i.details_id.toString())
                .build()

            request = Request.Builder()
                .url(url + "update_order_details_status.php")
                .get()
                .post(formBody)
                .build()

            client.newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    Log.e("onUpdateDetails", e.message.toString())
                    listener.onBooleanResult(UtilsEnum.DETAILS_DEFAULT)
                }

                override fun onResponse(call: Call, response: Response) {
                    try{
                        if(index == arrayDetails.size-1) {
                            listener.onBooleanResult(UtilsEnum.DETAILS_UPDATE)
                        }
                    } catch (ex : Exception){
                        Log.e("onUpdateDetails", ex.message.toString())
                        listener.onBooleanResult(UtilsEnum.DETAILS_DEFAULT)
                    }
                }
            })
        }
    }


    fun onLoadOrderId(orderId : String, listener : IListenerDetails){
        val client = OkHttpClient()

        val formBody = FormBody.Builder()
            .add("details_order", orderId)
            .build()

        val request = Request.Builder()
            .url(url + "select_order_details_phone.php")
            .get()
            .post(formBody)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.e("onLoadOrderId", e.message.toString())
                listener.onArrayDetails(null)
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
                        var element : OrderDetails
                        val array : ArrayList<OrderDetails> = ArrayList()
                        for(i in 0 until obj.length()){
                            data = obj.getJSONObject(i)
                            element =
                                OrderDetails(
                                    data.getInt("details_id"),
                                    data.getInt("details_prod"),
                                    data.getDouble("details_count"),
                                    data.getInt("details_order"),
                                    data.getInt("details_status"),
                                    data.getString( "details_comment"),
                                    null,
                                    Product(
                                        data.getInt("prod_id"),
                                        data.getString("prod_name"),
                                        data.getDouble("prod_price"),
                                        data.getDouble("prod_count"),
                                        data.getInt("prod_value"),
                                        data.getInt("prod_category"),
                                        data.getDouble("prod_start_price"),
                                        data.getInt("prod_kitchen"),
                                        0,
                                        data.getInt("prod_presence")
                                    )
                                )
                            array.add(element)
                        }
                        listener.onArrayDetails(array)
                    }else
                        listener.onArrayDetails(ArrayList())

                } catch (ex : Exception){
                    Log.e("onLoadOrderId", ex.message.toString())
                    listener.onArrayDetails(null)
                }
            }
        })
    }

    fun onLoadStatus(detailsStatus : String, prodKitchen : String, listener : IListenerDetails){
        val client = OkHttpClient()

        val formBody = FormBody.Builder()
            .add("details_status", detailsStatus)
            .add("prod_kitchen", prodKitchen)
            .build()

        val request = Request.Builder()
            .url(url + "select_status_details_phone.php")
            .get()
            .post(formBody)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.e("onLoadStatus", e.message.toString())
                listener.onArrayDetails(null)
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
                        var element : OrderDetails
                        val array : ArrayList<OrderDetails> = ArrayList()
                        for(i in 0 until obj.length()){
                            data = obj.getJSONObject(i)
                            element =
                                OrderDetails(
                                    data.getInt("details_id"),
                                    data.getInt("details_prod"),
                                    data.getDouble("details_count"),
                                    data.getInt("details_order"),
                                    data.getInt("details_status"),
                                    data.getString( "details_comment"),
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
                                            data.getString("table_time")
                                        ),
                                        User(
                                            data.getInt("user_id"),
                                            data.getString("user_name"),
                                            data.getString("user_password"),
                                            data.getInt("user_role")
                                        )
                                    ),
                                    Product(
                                        data.getInt("prod_id"),
                                        data.getString("prod_name"),
                                        data.getDouble("prod_price"),
                                        data.getDouble("prod_count"),
                                        data.getInt("prod_value"),
                                        data.getInt("prod_category"),
                                        data.getDouble("prod_start_price"),
                                        data.getInt("prod_kitchen"),
                                        0,
                                        data.getInt("prod_presence")
                                    )
                                )
                            array.add(element)
                        }
                        listener.onArrayDetails(array)
                    }else
                        listener.onArrayDetails(ArrayList())
                } catch (ex : Exception){
                    Log.e("onLoadStatus", ex.message.toString())
                    listener.onArrayDetails(null)
                }
            }
        })
    }

    fun onLoadOrderKitchen(order_id : String, kitchen : String, listener : IListenerDetails){
        val client = OkHttpClient()

        val formBody = FormBody.Builder()
            .add("order_id", order_id)
            .add("prod_kitchen", kitchen)
            .build()

        val request = Request.Builder()
            .url(url + "select_kitchen_details_phone.php")
            .get()
            .post(formBody)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.e("onLoadMyOrder", e.message.toString())
                listener.onArrayDetails(null)
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
                        var element : OrderDetails
                        val array : ArrayList<OrderDetails> = ArrayList()
                        for(i in 0 until obj.length()){
                            data = obj.getJSONObject(i)
                            element =
                                OrderDetails(
                                    data.getInt("details_id"),
                                    data.getInt("details_prod"),
                                    data.getDouble("details_count"),
                                    data.getInt("details_order"),
                                    data.getInt("details_status"),
                                    data.getString( "details_comment"),
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
                                    ),
                                    Product(
                                        data.getInt("prod_id"),
                                        data.getString("prod_name"),
                                        data.getDouble("prod_price"),
                                        data.getDouble("prod_count"),
                                        data.getInt("prod_value"),
                                        data.getInt("prod_category"),
                                        data.getDouble("prod_start_price"),
                                        data.getInt("prod_kitchen"),
                                        0,
                                        data.getInt("prod_presence")
                                    )
                                )
                            array.add(element)
                        }
                        listener.onArrayDetails(array)
                    }else
                        listener.onArrayDetails(ArrayList())
                } catch (ex : Exception){
                    Log.e("onLoadMyOrder", ex.message.toString())
                    listener.onArrayDetails(null)
                }
            }
        })
    }

    fun onLoadActive(orderId : String, listener : IListenerDetails){
        val client = OkHttpClient()

        val formBody = FormBody.Builder()
            .add("details_order", orderId)
            .build()

        val request = Request.Builder()
            .url(url + "select_active_details_phone.php")
            .get()
            .post(formBody)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.e("onLoadActive", e.message.toString())
                listener.onArrayDetails(null)
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
                        var element : OrderDetails
                        val array : ArrayList<OrderDetails> = ArrayList()
                        for(i in 0 until obj.length()){
                            data = obj.getJSONObject(i)
                            element =
                                OrderDetails(
                                    data.getInt("details_id"),
                                    data.getInt("details_prod"),
                                    data.getDouble("details_count"),
                                    data.getInt("details_order"),
                                    data.getInt("details_status"),
                                    data.getString( "details_comment")
                                )
                            array.add(element)
                        }
                        listener.onArrayDetails(array)
                    }else
                        listener.onArrayDetails(null)
                } catch (ex : Exception){
                    Log.e("onLoadActive", ex.message.toString())
                    listener.onArrayDetails(null)
                }
            }
        })
    }
}