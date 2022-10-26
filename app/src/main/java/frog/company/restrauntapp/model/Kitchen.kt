package frog.company.restrauntapp.model

import java.io.Serializable

data class Kitchen(
    var kitchen_id : Int,
    var kitchen_name : String,

    var product : ArrayList<Product>? = null
): Serializable
