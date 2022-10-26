package frog.company.restrauntapp.model

import java.io.Serializable

data class Posting(
    var post_id : Int,
    var post_stock_id : Int,
    var post_value : Double,
    var post_type : Int,
    var post_date : String,
    var post_categories : Int,

    var type : Type ,
    var stock : Stock
) : Serializable
