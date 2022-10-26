package frog.company.restrauntapp.model

import java.io.Serializable

data class OrderDetails(
    var details_id : Int,
    var details_prod : Int,
    var details_count : Double,
    var details_order : Int,
    var details_status : Int,
    var details_comment : String,
    var order : Order? = null,
    var product : Product? = null
): Serializable
