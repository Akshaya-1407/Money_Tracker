package com.example.trackermoney.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.trackermoney.data.model.ExpenseEntity
import com.example.trackermoney.data.model.ExpenseSummary
import kotlinx.coroutines.flow.Flow

@Dao
interface ExpenseDao {

    @Query("SELECT * FROM expense_table")
    fun getAllExpenses(): Flow<List<ExpenseEntity>>

    @Query("SELECT * FROM expense_table where type = 'Expense' ORDER BY amount DESC LIMIT 5")
    fun getTopExpenses(): Flow<List<ExpenseEntity>>

    @Insert
    suspend fun insertExpense(expense: ExpenseEntity)

    @Delete
    suspend fun deleteExpense(expense: ExpenseEntity)

    @Update
    suspend fun updateExpense(expense: ExpenseEntity)

    @Query("SELECT type,date, SUM(amount) AS total_amount FROM expense_table where type = :type GROUP BY type,date ORDER BY date ")
    fun getAllExpensesByDate(type:String = "Expense"): Flow<List<ExpenseSummary>>
}