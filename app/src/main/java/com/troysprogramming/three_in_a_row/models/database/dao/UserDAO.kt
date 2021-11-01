package com.troysprogramming.three_in_a_row.models.database.dao

import androidx.room.*
import com.troysprogramming.three_in_a_row.models.User

@Dao
interface UserDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun createUser(vararg user: User) : Unit

    @Query("SELECT * FROM user")
    fun getUsers() : List<User>

    @Query("SELECT * FROM user WHERE id = :id")
    fun getUser(id: Int) : User

    @Query("SELECT * FROM user WHERE userName = :userName")
    fun getUsersUnderUsernameResult(userName: String) : List<User>

    @Delete
    fun deleteScore(user: User) : Unit

    @Query("SELECT MAX(id) FROM score")
    fun getHighestId() : Int
}