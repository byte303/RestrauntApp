package frog.company.restrauntapp.inter

import frog.company.restrauntapp.model.OrderDetails

interface IListenerDetails {
    fun onBooleanResult(result : Int)
    fun onIndexResult(result : Int)
    fun onArrayDetails(result : ArrayList<OrderDetails>?)
    fun onResultDetail(result : OrderDetails?)
}