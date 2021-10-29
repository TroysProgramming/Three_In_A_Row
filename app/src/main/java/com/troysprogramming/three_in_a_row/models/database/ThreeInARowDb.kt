package com.troysprogramming.three_in_a_row.models.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.troysprogramming.three_in_a_row.models.game.HighScore

@Database(
    entities = [HighScore::class],
    version = 1,
    exportSchema = false
)

abstract class ThreeInARowDb : RoomDatabase() {

    abstract fun scoreDao() : HighScoreDAO

    companion object {
        private var threeInARowDb : ThreeInARowDb? = null

        fun getDbInstance(context: Context) : ThreeInARowDb {
            if(threeInARowDb == null) {
                threeInARowDb = Room.databaseBuilder(
                    context.applicationContext, ThreeInARowDb::class.java, "high_score.db")
                    .allowMainThreadQueries()
                    .build()
            }

            return threeInARowDb as ThreeInARowDb
        }
    }
}