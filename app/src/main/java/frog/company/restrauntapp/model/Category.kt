package frog.company.restrauntapp.model

import java.io.Serializable

data class Category(
    var category_id : Int,
    var category_name : String,
    var product : ArrayList<Product>? = null
): Serializable
