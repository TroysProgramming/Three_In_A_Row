package com.troysprogramming.three_in_a_row.models.database

import androidx.room.*
import com.troysprogramming.three_in_a_row.models.game.HighScore

@Dao
interface HighScoreDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun createScores(vararg highScore: HighScore) : Unit

    @Query("SELECT * FROM score ORDER BY time ASC")
    fun getTopTenScores() : List<HighScore>

    @Delete
    fun deleteScore(highScore: HighScore) : Unit

    @Query("SELECT MAX(id) FROM score")
    fun getHighestId() : Int
}