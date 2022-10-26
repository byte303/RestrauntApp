package frog.company.restrauntapp.model

import java.io.Serializable

data class Stock(
    var stock_id : Int,
    var stock_name : String,
    var stock_count : Double,
    var stock_price : Double,
    var stock_value : Int,
    var stock_category : Int,

    var  type : Type? = null
): Serializable
