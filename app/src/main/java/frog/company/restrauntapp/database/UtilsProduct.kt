package frog.company.restrauntapp.database

import android.app.Activity
import android.util.Log
import android.widget.Toast
import frog.company.restrauntapp.enum.EnumDetailsStatus
import frog.company.restrauntapp.helper.Converting
import frog.company.restrauntapp.helper.UtilsEnum
import frog.company.restrauntapp.inter.IListenerDetails
import frog.company.restrauntapp.inter.IListenerOrder
import frog.company.restrauntapp.inter.IListenerProduct
import frog.company.restrauntapp.model.*
import okhttp3.*
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException

class UtilsProduct() {

    private val url = UtilsDB.mainUrl + "product/"

    fun onLoad(listener : IListenerProduct, presence : Boolean = false){
        val client = OkHttpClient()

        val address = if(!presence)
            "load_product_phone_presence.php"
        else
            "load_product_phone.php"

        val request = Request.Builder()
            .url(url + address)
            .get()
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.e("onLoadProduct", e.message.toString())
                listener.onArrayProducts(null)
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
                        var product : Product
                        val array : ArrayList<Product> = ArrayList()
                        for(i in 0 until obj.length()){
                            data = obj.getJSONObject(i)
                            product =
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
                                    data.getInt("prod_presence"),
                                    "",
                                    EnumDetailsStatus.NewOrder.num,
                                    Type(
                                        data.getInt("type_id"),
                                        data.getString("type_name")
                                    ),
                                    Category(
                                        data.getInt("category_id"),
                                        data.getString("category_name")
                                    )
                                )
                            array.add(product)
                        }
                        listener.onArrayProducts(array)
                    }else
                        listener.onArrayProducts(null)
                } catch (ex : Exception){
                    Log.e("onLoadProduct", ex.message.toString())
                    listener.onArrayProducts(null)
                }
            }
        })
    }

    fun onLoadKitchen(prod_kitchen : Int, listener : IListenerProduct){
        val client = OkHttpClient()

        val formBody = FormBody.Builder()
            .add("prod_kitchen", prod_kitchen.toString())
            .build()

        val request = Request.Builder()
            .url(url + "load_product_kitchen_phone.php")
            .get()
            .post(formBody)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.e("onLoadProduct", e.message.toString())
                listener.onArrayProducts(null)
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
                        var product : Product
                        val array : ArrayList<Product> = ArrayList()
                        for(i in 0 until obj.length()){
                            data = obj.getJSONObject(i)
                            product =
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
                                    data.getInt("prod_presence"),
                                    "",
                                    EnumDetailsStatus.NewOrder.num,
                                    Type(
                                        data.getInt("type_id"),
                                        data.getString("type_name")
                                    ),
                                    Category(
                                        data.getInt("category_id"),
                                        data.getString("category_name")
                                    )
                                )
                            array.add(product)
                        }
                        listener.onArrayProducts(array)
                    }else
                        listener.onArrayProducts(null)
                } catch (ex : Exception){
                    Log.e("onLoadProduct", ex.message.toString())
                    listener.onArrayProducts(null)
                }
            }
        })
    }


    fun onUpdate(arrayProduct : ArrayList<Product>, listener : IListenerProduct){
        val client = OkHttpClient()
        var formBody : FormBody
        var request : Request

        for((index, i) in arrayProduct.withIndex()){
            formBody = FormBody.Builder()
                .add("prod_presence", i.prod_presence.toString())
                .add("prod_id", i.prod_id.toString())
                .build()

            request = Request.Builder()
                .url(url + "update_product_presence.php")
                .get()
                .post(formBody)
                .build()

            client.newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    Log.e("onUpdateProduct", e.message.toString())
                    listener.onIndexResult(UtilsEnum.PRODUCT_DEFAULT)
                }

                override fun onResponse(call: Call, response: Response) {
                    try{
                        if(index == arrayProduct.size-1) {
                            listener.onIndexResult(UtilsEnum.PRODUCT_UPDATE)
                        }
                    } catch (ex : Exception){
                        Log.e("onUpdateProduct", ex.message.toString())
                        listener.onIndexResult(UtilsEnum.PRODUCT_DEFAULT)
                    }
                }
            })
        }
    }
}