package com.troysprogramming.three_in_a_row.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.troysprogramming.three_in_a_row.models.database.SQLiteService
import com.troysprogramming.three_in_a_row.models.game.HighScore

class HighScoreViewModel : ViewModel() {

    private var scoreList : ArrayList<HighScore> = SQLiteService.getInstance().getTopTenScores()
    private var scores: ArrayList<MutableLiveData<HighScore>> = ArrayList()

    init {
        for(i in 0 until scoreList.size) {
            scores.add(MutableLiveData<HighScore>())
            scores[i].value = scoreList[i]
        }
    }

    fun getScores(i: Int): MutableLiveData<HighScore> { return scores[i] }
    fun getScoreListLength(): Int { return scores.size }
}