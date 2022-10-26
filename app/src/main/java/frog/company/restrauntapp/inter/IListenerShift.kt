package frog.company.restrauntapp.inter

import frog.company.restrauntapp.model.Shift

interface IListenerShift {
    fun onSelectShift(result : Shift?)
}