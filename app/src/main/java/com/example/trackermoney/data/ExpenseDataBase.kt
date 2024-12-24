package com.example.trackermoney.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.trackermoney.data.dao.ExpenseDao
import com.example.trackermoney.data.model.ExpenseEntity


@Database(entities = [ExpenseEntity::class], version = 1, exportSchema = false)
abstract class ExpenseDataBase: RoomDatabase()  {
    abstract fun expenseDao(): ExpenseDao

    companion object {
        const val DATABASE_NAME = "expense_database"

        @JvmStatic
        fun getDatabase(context: Context): ExpenseDataBase {
            return Room.databaseBuilder(
                context,
                ExpenseDataBase::class.java,
                DATABASE_NAME
            )
//                .addCallback(object : Callback() {
//                override fun onCreate(db: SupportSQLiteDatabase) {
//                    super.onCreate(db)
//                }
//
//            }
//            )
                .build()
        }
    }
}