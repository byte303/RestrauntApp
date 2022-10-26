package frog.company.restrauntapp.inter

import frog.company.restrauntapp.helper.UtilsEnum
import frog.company.restrauntapp.model.Order

interface IListenerOrder {
    fun onListOrders(result : ArrayList<Order>?)
    fun onOrder(result : Order?, index : Int = UtilsEnum.ORDER_DEFAULT)
    fun onResultOrder(boolean : Boolean, result : Int = UtilsEnum.ORDER_DEFAULT)
}