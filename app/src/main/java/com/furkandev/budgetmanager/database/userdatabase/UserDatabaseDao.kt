package com.furkandev.budgetmanager.database.userdatabase

import androidx.room.*
import com.furkandev.budgetmanager.model.User

@Dao
interface UserDatabaseDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertUser(user: User)

    @Query("UPDATE user_informations_table SET user_name = :userName, user_gender = :userGender WHERE user_id = 0")
    fun updateUser(userName: String, userGender: Byte)

    @Query("SELECT * FROM user_informations_table LIMIT 1")
    fun getUser() : User?
}