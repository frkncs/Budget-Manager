package com.furkandev.budgetmanager.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_informations_table")
data class User(

    @ColumnInfo(name = "user_id")
    @PrimaryKey(autoGenerate = false)
    val userId: Long = 0,

    @ColumnInfo(name = "user_name")
    var name: String = "-1",

    @ColumnInfo(name = "user_gender")
    var gender: Byte = 2, // 0: Male, 1: Female, 2: Unknown

)