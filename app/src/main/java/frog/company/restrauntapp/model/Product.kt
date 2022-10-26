package frog.company.restrauntapp.model

import frog.company.restrauntapp.enum.EnumDetailsStatus
import java.io.Serializable

data class Product(
    var prod_id : Int,
    var prod_name : String,
    var prod_price : Double,
    var prod_count : Double,
    var prod_value : Int,
    var prod_category : Int,
    var prod_start_price : Double,
    var prod_kitchen : Int,
    var prod_total : Int,
    var prod_presence : Int,
    var prod_comment : String = "",
    var prod_status : Int = EnumDetailsStatus.NewOrder.num,

    var type : Type? = null,
    var category : Category? = null,
    var recipe : ArrayList<Recipe>? = null,
    var orderDetails : ArrayList<OrderDetails>? = null,
    var kitchen : Kitchen? = null
) : Serializable
