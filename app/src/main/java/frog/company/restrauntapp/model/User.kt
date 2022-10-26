package frog.company.restrauntapp.model

import java.io.Serializable

data class User(
    var user_id : Int = 0,
    var user_name : String = "",
    var user_password : String = "",
    var user_role : Int = 0,
    var user_kitchen : Int = 0,

    var shift : Shift? = null,
    var kitchen : Kitchen? = null): Serializable
