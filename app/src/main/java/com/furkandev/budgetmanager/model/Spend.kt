package com.furkandev.budgetmanager.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "spends_table")
data class Spend(
        @PrimaryKey(autoGenerate = true)
        @ColumnInfo(name = "spend_id")
        val spendId: Long = 0L,

        @ColumnInfo(name = "spend_name")
        var name: String = "",

        @ColumnInfo(name = "spend_price")
        var price: String = "",

        @ColumnInfo(name = "spend_type")
        var spendType: Int = 0,

        @ColumnInfo(name = "spend_currency_unity")
        var spendCurrencyUnit: Int = 0
)