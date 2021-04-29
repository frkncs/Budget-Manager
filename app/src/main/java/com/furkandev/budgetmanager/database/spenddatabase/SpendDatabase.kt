package com.furkandev.budgetmanager.database.spenddatabase

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.furkandev.budgetmanager.model.Spend

@Database(entities = [Spend::class], version = 1, exportSchema = false)
abstract class SpendDatabase : RoomDatabase(){

    abstract val spendDatabaseDao : SpendDatabaseDao

    companion object{

        private var INSTANCE : SpendDatabase? = null

        fun getInstance (context: Context) : SpendDatabase {

            var instance = INSTANCE

            if (instance == null){
                instance = Room.databaseBuilder(
                    context.applicationContext,
                    SpendDatabase::class.java,
                    "spend_history_database"
                )
                    .allowMainThreadQueries()
                    .build()

                INSTANCE = instance
            }
            return instance
        }
    }
}