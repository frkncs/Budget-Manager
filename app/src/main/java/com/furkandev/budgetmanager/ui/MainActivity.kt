package com.furkandev.budgetmanager.ui

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.furkandev.budgetmanager.R
import com.furkandev.budgetmanager.database.userdatabase.UserDatabase
import com.furkandev.budgetmanager.model.User

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initializeDefaultUserInformations()
    }

    private fun initializeDefaultUserInformations() {
        if (checkUserOpenFirstTime()) {
            val userDao = UserDatabase.getInstance(this).userDatabaseDao

            val defaultUser = User()

            defaultUser.name = "-1" // No name
            defaultUser.gender = 2 // Unknown gender

            userDao.insertUser(defaultUser)
        }
    }

    private fun checkUserOpenFirstTime(): Boolean {

        val sharedPreferences = this.getSharedPreferences("com.furkandev.budgetmanager", Context.MODE_PRIVATE)

        if (sharedPreferences.getBoolean("isOpenFirstTime",true)) {
            return true
        }

        return false
    }
}