package com.example.trackermoney.viewmodel

import android.content.Context
import android.icu.text.UnicodeSet.EntryRange
import android.os.DropBoxManager
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.trackermoney.R
import com.example.trackermoney.Utils
import com.example.trackermoney.data.ExpenseDataBase
import com.example.trackermoney.data.dao.ExpenseDao
import com.example.trackermoney.data.model.ExpenseEntity
import com.example.trackermoney.data.model.ExpenseSummary
import java.security.KeyStore
import java.security.KeyStore.Entry
import java.util.Map
var baseTime: Long = 0L
class StatsViewModel(dao: ExpenseDao) : ViewModel(){
    val entries = dao.getAllExpensesByDate()
    val topExpenses = dao.getTopExpenses()


    fun getEntriesForChart(entries:List<ExpenseSummary>) : List<com.github.mikephil.charting.data.Entry> {
        val list = mutableListOf<com.github.mikephil.charting.data.Entry>()
        if(entries.isEmpty()) return list

        baseTime = Utils.getMillisFromDate(entries[0].date)
        for (entry in entries){
            val formattedDate = Utils.getMillisFromDate(entry.date) - baseTime
//            println("Date: ${entry.date}, Normalized X: $formattedDate")
            list.add(com.github.mikephil.charting.data.Entry(formattedDate.toFloat(), entry.total_amount.toFloat()))
        }
        return list
    }
}

class StatsViewModelFactory(private val context: Context) : ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(StatsViewModel::class.java)) {
            val dao = ExpenseDataBase.getDatabase(context).expenseDao()
            @Suppress("UNCHECKED_CAST")
            return StatsViewModel(dao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}