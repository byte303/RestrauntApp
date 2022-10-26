package frog.company.restrauntapp.inter

import frog.company.restrauntapp.model.Hall

interface IListenerHall {
    fun onListHalls(result: ArrayList<Hall>?)
    fun onSelectHall(result : Hall?)
}