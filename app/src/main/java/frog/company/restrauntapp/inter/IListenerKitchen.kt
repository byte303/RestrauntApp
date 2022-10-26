package frog.company.restrauntapp.inter

import frog.company.restrauntapp.model.Kitchen

interface IListenerKitchen {
    fun onResultKitchenAll(result : ArrayList<Kitchen>?)
    fun onResultKitchen(result : Kitchen?)
}