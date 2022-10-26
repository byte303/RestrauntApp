package frog.company.restrauntapp.model

import java.io.Serializable

data class Shift(
    var shift_id : Int,
    var shift_date_open : String,
    var shift_date_close : String,
    var shift_user_id : Int,
    var shift_status : Int,

    var user : User? = null
) : Serializable
