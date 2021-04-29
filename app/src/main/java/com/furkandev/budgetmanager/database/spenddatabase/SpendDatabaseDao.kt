package com.furkandev.budgetmanager.database.spenddatabase

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.furkandev.budgetmanager.model.Spend

@Dao
interface SpendDatabaseDao {

    @Insert
    fun insertSpend(spend: Spend)

    @Update
    fun updateSpend(spend: Spend)

    @Query("SELECT * FROM spends_table WHERE spend_id = :key")
    fun getSpendWithID(key: Long) : Spend?

    @Query("SELECT * FROM spends_table")
    fun getAllSpends() : List<Spend>

    @Query("DELETE FROM spends_table WHERE spend_id = :key")
    fun deleteSpendWithID(key: Long)

    @Query("DELETE FROM spends_table")
    fun deleteAllSpends()
}