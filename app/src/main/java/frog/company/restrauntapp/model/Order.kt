package frog.company.restrauntapp.model

import frog.company.restrauntapp.R
import frog.company.restrauntapp.enum.EnumDetailsStatus
import frog.company.restrauntapp.enum.EnumOrderStatus
import java.io.Serializable

data class Order(
    var order_id : Int,
    var order_table : Int,
    var order_date : String,
    var order_close_date : String,
    var order_price : Double,
    var order_discount : Double,
    var order_user : Int,
    var order_shift : Int,
    var order_payment : Int,
    var order_status : Int,
    var order_delivery : Int,
    var order_comment : String,
    var order_status_cook : Int,
    var order_favorite : Int,
    var order_price_waiter : Double,
    var table: Table? = null,
    var user : User? = null,
    var orderDetails : ArrayList<OrderDetails>? = null
): Serializable {
    fun onCloseDate() : String{
        return if (order_status == EnumOrderStatus.Paid.num) order_close_date else "-"
    }

    fun onOrderStatus() : String{
        return if(order_status == EnumOrderStatus.Paid.num) "Оплачен" else "Не оплачен"
        /*
        return when(order_status){
            EnumOrderStatus.Paid.num -> "Оплачен"
            EnumOrderStatus.Ready.num -> "Готов"
            EnumOrderStatus.Filed.num -> "Подан"
            EnumOrderStatus.Cooking.num -> "Готовится"
            EnumOrderStatus.Return.num -> "Возврат"
            else -> "Неизвестно"
        }*/
    }
}