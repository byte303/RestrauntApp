package frog.company.restrauntapp.inter

import frog.company.restrauntapp.model.Table

interface IListenerTable {
    fun onListTables(result : ArrayList<Table>?)
    fun onSelectTable(result : Table?)
    fun onResultTable(boolean: Boolean)
}