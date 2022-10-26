package frog.company.restrauntapp.inter

import frog.company.restrauntapp.model.User

interface IListenerUser {
    fun onSelectUser(user : User?)
    fun onSelectAllUser(user : ArrayList<User>?)
    fun onUserBooleanResult(boolean: Boolean)
}