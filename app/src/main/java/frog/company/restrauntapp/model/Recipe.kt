package frog.company.restrauntapp.model

import java.io.Serializable

data class Recipe(
    var recipe_id : Int,
    var recipe_stock : Int,
    var recipe_product : Int,
    var recipe_count : Double,
    var recipe_value : Int,

    var type : Type? = null,
    var stock : Stock? = null,
    var product : Product? = null
): Serializable
