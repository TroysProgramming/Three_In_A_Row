package com.troysprogramming.three_in_a_row.models.database

import android.content.Context
import com.troysprogramming.three_in_a_row.models.User
import com.troysprogramming.three_in_a_row.models.game.HighScore

class SQLiteService private constructor(context: Context) {

    init {
        db = ThreeInARowDb.getDbInstance(context)
    }

    companion object {
        private var sqliteService : SQLiteService? = null

        private lateinit var db : ThreeInARowDb

        fun createNewInstance(context: Context) : Unit {
            sqliteService = SQLiteService(context)
        }

        fun getInstance() : SQLiteService {
            return sqliteService as SQLiteService
        }
    }

    fun getTopTenScores() : ArrayList<HighScore> {
        return db.scoreDao().getTopTenScores() as ArrayList<HighScore>
    }

    fun createScore(highScore : HighScore) : Unit {
        db.scoreDao().createScores(highScore)
    }

    fun getHighestScoreID() : Int { return db.scoreDao().getHighestId() }

    fun createUser(user: User) { db.userDao().createUser(user) }

    fun getUsersUnderUsernameResult(userName: String) : List<User> {
        return db.userDao().getUsersUnderUsernameResult(userName)
    }

    fun getHighestUserID() : Int { return db.userDao().getHighestId() }
}