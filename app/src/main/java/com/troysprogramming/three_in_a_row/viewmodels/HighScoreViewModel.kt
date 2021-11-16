package com.troysprogramming.three_in_a_row.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.troysprogramming.three_in_a_row.models.database.SQLiteService
import com.troysprogramming.three_in_a_row.models.game.HighScore

class HighScoreViewModel : ViewModel() {

    private var scoreList : MutableLiveData<ArrayList<HighScore>> =
        MutableLiveData<ArrayList<HighScore>>()
    private var isUserScores: MutableLiveData<Boolean> = MutableLiveData<Boolean>()

    init {
        isUserScores.value = false
        scoreList.value = SQLiteService.getInstance().getTopTenScores()
        displayScores()
    }

    fun getIsUserScores(): MutableLiveData<Boolean> { return isUserScores }
    fun getScores(): MutableLiveData<ArrayList<HighScore>> { return scoreList }
    fun getScoreListLength(): Int { return scoreList.value!!.size }

    fun swapScores() {
        isUserScores.value = !isUserScores.value!!
        displayScores()
    }

    private fun displayScores() {
        scoreList.value = if(isUserScores.value!!)
            SQLiteService.getInstance().getUsersTopTenScores()
        else
            SQLiteService.getInstance().getTopTenScores()
    }
}