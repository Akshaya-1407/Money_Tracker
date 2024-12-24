package com.example.trackermoney.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.trackermoney.R
import com.example.trackermoney.Utils
import com.example.trackermoney.data.ExpenseDataBase
import com.example.trackermoney.data.dao.ExpenseDao
import com.example.trackermoney.data.model.ExpenseEntity

class HomeViewModel(dao: ExpenseDao) : ViewModel(){
    val expenses = dao.getAllExpenses()

    fun getBalance(list : List<ExpenseEntity>): String {
        var total = 0.0
        list.forEach {
            if(it.type == "Income"){
                total += it.amount
            }else{
                total -= it.amount
            }
        }
        return "${Utils.formatToDecimalValue(total)}"
    }
    fun getTotalIncome(list : List<ExpenseEntity>): String {
        var total = 0.0
        list.forEach {
            if (it.type == "Income") {
                total += it.amount
            }
        }
        return "${Utils.formatToDecimalValue(total)}"
    }
    fun getTotalExpense(list : List<ExpenseEntity>): String {
        var total = 0.0
        list.forEach {
            if (it.type == "Expense") {
                total += it.amount
            }
        }
        return "${Utils.formatToDecimalValue(total)}"
    }
//    fun getItemIcon(item: ExpenseEntity): Int {
//        if(item.category == "Salary"){
//            return R.drawable.ic_salary
//        }
//        else{
//            return R.drawable.ic_salary
//        }
//    }
}

class HomeViewModelFactory(private val context: Context) : ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            val dao = ExpenseDataBase.getDatabase(context).expenseDao()
            @Suppress("UNCHECKED_CAST")
            return HomeViewModel(dao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}