package com.troysprogramming.three_in_a_row.viewmodels

import android.graphics.Color
import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.troysprogramming.three_in_a_row.models.User
import com.troysprogramming.three_in_a_row.models.database.SQLiteService
import com.troysprogramming.three_in_a_row.models.game.Game
import com.troysprogramming.three_in_a_row.models.game.GridItem
import com.troysprogramming.three_in_a_row.models.game.HighScore
import kotlin.collections.ArrayList

class GameViewModel(private val colour1: Int, private val colour2: Int, gridSizeStr: String)
    : ViewModel(), Game.GameTimeListener {

    private var game: Game = Game(colour1, colour2, gridSizeStr[0].digitToInt())

    private var gameSeconds: MutableLiveData<String> = MutableLiveData<String>()
    private var gameMinutes: MutableLiveData<String> = MutableLiveData<String>()
    private var gameMessage: MutableLiveData<String> = MutableLiveData<String>()
    private var gameMessageColour: MutableLiveData<Int> = MutableLiveData<Int>()
    private var isPlayerOnesTurn: MutableLiveData<Boolean> = MutableLiveData<Boolean>()
    private var gameEnded: MutableLiveData<Boolean> = MutableLiveData<Boolean>()
    private var gameGrid: ArrayList<ArrayList<MutableLiveData<GridItem>>> = ArrayList()
    private var defaultColor: Int = Color.GRAY

    init {
        gameSeconds.value = game.getSeconds()
        gameMinutes.value = game.getMinutes()
        gameMessage.value = game.getMessage()
        gameMessageColour.value = game.getCurrentColour()
        isPlayerOnesTurn.value = game.checkIfPlayerOnesTurn()
        gameEnded.value = game.getGameEnded()
        mutableGameBoard()
        game.addTimeListener(this)
    }

    private fun mutableGameBoard() {
        for(i in game.getBoard().indices) {

            gameGrid.add(ArrayList())

            for(j in game.getBoard()[0].indices) {
                gameGrid[i].add(MutableLiveData<GridItem>())
                gameGrid[i][j].value = game.getBoard()[i][j]
            }
        }
    }

    fun onGridItemClick(x: Int, y: Int) {
        game.onGridItemClick(x, y)
        gameGrid[x][y].postValue(game.getBoard()[x][y])
        isPlayerOnesTurn.postValue(game.checkIfPlayerOnesTurn())
        gameEnded.postValue(game.getGameEnded())
        gameMessage.postValue(game.getMessage())
        gameMessageColour.postValue(game.getCurrentColour())
    }

    fun getColourAt(x: Int, y: Int): Int {
        return when(game.getOccupation(x, y)) {
            GridItem.Companion.Occupation.PLAYER1 -> colour1
            GridItem.Companion.Occupation.PLAYER2 -> colour2
            else -> defaultColor
        }
    }

    fun findIDIn2DArray(tag: Int, arr: ArrayList<ArrayList<View>>) : Array<Int> {
        for(i in arr.indices)
        {
            for(j in arr[i].indices)
            {
                if(arr[i][j].tag == tag)
                    return arrayOf(i, j)
            }
        }

        return arrayOf(-1, -1)
    }

    fun showCurrentTurn(colourView: View)
    {
        colourView.setBackgroundColor(
            if(game.checkIfPlayerOnesTurn())
                colour1
            else
                colour2
        )
    }

    fun getMinutes() : MutableLiveData<String> { return gameMinutes }
    fun getSeconds() : MutableLiveData<String> { return gameSeconds }
    fun getMessage() : MutableLiveData<String> { return gameMessage }
    fun getMessageColour(): MutableLiveData<Int> { return gameMessageColour }
    fun isPlayerOnesTurn(): MutableLiveData<Boolean> { return isPlayerOnesTurn }
    fun getGameEnded(): MutableLiveData<Boolean> { return gameEnded }
    fun getTile(x: Int, y: Int): MutableLiveData<GridItem> { return gameGrid[x][y] }
    fun getColour1(): Int { return colour1 }
    fun getColour2(): Int { return colour2 }
    fun getDefaultColour(): Int { return defaultColor }

    override fun onTick() {
        gameSeconds.postValue(game.getSeconds())
        gameMinutes.postValue(game.getMinutes())
    }

    override fun onMessageClear() {
        gameMessage.postValue(game.getMessage())
    }
}