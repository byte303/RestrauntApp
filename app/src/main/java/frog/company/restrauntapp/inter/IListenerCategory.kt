package frog.company.restrauntapp.inter

import frog.company.restrauntapp.model.Category

interface IListenerCategory {
    fun onResultCategory(category: Category)
    fun onResultCategoryAll(category: ArrayList<Category>?)
}