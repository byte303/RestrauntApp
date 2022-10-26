package frog.company.restrauntapp.database

import android.util.Log
import frog.company.restrauntapp.inter.IListenerCategory
import frog.company.restrauntapp.model.Category
import okhttp3.*
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException

class UtilsCategory() {

    private val url = UtilsDB.mainUrl + "category/"

    fun onLoadCategory(listener : IListenerCategory){
        val client = OkHttpClient()

        val request = Request.Builder()
            .url(url + "load_category.php")
            .get()
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.e("onLoadCategory", e.message.toString())
                listener.onResultCategoryAll(null)
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
                        var category : Category
                        val arrayCategory : ArrayList<Category> = ArrayList()
                        for(i in 0 until obj.length()){
                            data = obj.getJSONObject(i)
                            category =
                                Category(
                                    data.getInt("category_id"),
                                    data.getString("category_name")
                                )
                            arrayCategory.add(category)
                        }
                        listener.onResultCategoryAll(arrayCategory)
                    }else
                        listener.onResultCategoryAll(null)
                } catch (ex : Exception){
                    Log.e("onLoadCategory", ex.message.toString())
                    listener.onResultCategoryAll(null)
                }
            }
        })
    }
}