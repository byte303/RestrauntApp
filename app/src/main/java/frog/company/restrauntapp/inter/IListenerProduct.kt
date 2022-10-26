package frog.company.restrauntapp.inter

import frog.company.restrauntapp.model.Product

interface IListenerProduct {
    fun onArrayProducts(result : ArrayList<Product>?)
    fun onSelectProduct(result : Product, boolean: Boolean = false)
    fun onProductCount(count : Int, comment : String, id : Int)
    fun onIndexResult(index : Int)
}