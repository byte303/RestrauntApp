package frog.company.restrauntapp.model

import java.io.Serializable

data class Hall(
    var hall_id: Int,
    var hall_name: String,
    var hall_price: Double,
    var table: ArrayList<Table>? = null
): Serializable
