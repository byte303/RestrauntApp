package frog.company.restrauntapp.model

import java.io.Serializable

data class Table(
    var table_id : Int,
    var table_name : String,
    var table_place : Int,
    var table_hall_id : Int,
    var table_status : Int,
    var table_date : String,
    var table_time : String,
    var hall : Hall? = null
): Serializable