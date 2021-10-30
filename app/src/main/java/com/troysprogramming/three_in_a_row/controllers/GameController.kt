package com.troysprogramming.three_in_a_row.controllers

import android.content.Context
import android.content.SharedPreferences
import android.graphics.Color
import android.view.View
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.troysprogramming.three_in_a_row.R
import com.troysprogramming.three_in_a_row.models.database.SQLiteService
import com.troysprogramming.three_in_a_row.models.game.Game
import com.troysprogramming.three_in_a_row.models.game.GameVMFactory
import com.troysprogramming.three_in_a_row.models.game.GridItem
import com.troysprogramming.three_in_a_row.models.game.HighScore
import com.troysprogramming.three_in_a_row.views.GameActivity

class GameController(activity: GameActivity)
{
    private var gameActivity: GameActivity = activity
    private val sharedPref : SharedPreferences =
        gameActivity.getSharedPreferences("3inarow_settings.xml", Context.MODE_PRIVATE)
    private lateinit var game : Game

    private var colour1 : Int = 0
    private var colour2 : Int = 0
    private var defaultColor : Int = Color.GRAY
    private var gridSizeStr : String? = sharedPref.getString("grid_size", "4 x 4")
    private var gridSize = gridSizeStr!![0].digitToInt()

    fun setPlayerColours() {
        colour1 = sharedPref.getInt("colour_1", Color.RED)
        colour2 = sharedPref.getInt("colour_2", Color.BLUE)
    }

    fun generateGrid() {

        game = ViewModelProvider(gameActivity, GameVMFactory(gridSize)).get(Game::class.java)

        // pass grid size to view to display the grid
        gameActivity.renderGrid(gridSize)

        // add occupations to created tiles
        for(i in 0 until gameActivity.getGridItems().size)
        {
            for(j in 0 until gameActivity.getGridItems()[i].size)
            {
                setViewColour(gameActivity.getGridItems()[i][j].findViewById(R.id.view_colour),
                    game.getOccupation(i, j))
            }
        }

        if(game.isGameEnded())
            endGame()
    }

    fun onGridItemClick(clickedView: View, x: Int, y: Int, currentTurnView: View) {
        // notify the Game instance that a grid item in the passed co-ordinates has been touched
        val isValidMove : Boolean = game.checkValidMove(x, y)

        // if the move is valid,
        if(isValidMove)
        {
            // set the grid item's ownership to the current player
            game.changeOccupationToCurrentPlayer(x, y)

            // get the current player's turn
            val occupation : GridItem.Companion.Occupation = game.getOccupation(x, y)

            // set the tapped view to their colour
            setViewColour(clickedView, occupation)

            // check for three in a row
            var isGameOver : Boolean = game.checkForThree(x, y)

            // swap turns
            game.swapTurns()

            // if the game is over, show the winner
            if(isGameOver)
            {
                SQLiteService.getInstance().createScore(HighScore(
                    SQLiteService.getInstance().getHighestID() + 1,
                    0,
                    "Guest",
                    gameActivity.getTime(),
                    "01/01/2021",
                    gridSizeStr!!
                )
                )

                endGame()
            }

            // change the colour of the view showing the current player's turn
            showCurrentTurn(currentTurnView)
        }
        else
        {
            // if the move is not valid, present an error to the user.
            gameActivity.displayMessage(gameActivity.baseContext.resources
                .getString(R.string.invalidmove), Color.RED, false)
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

    fun endGame() {
        game.setGameEnded(true)

        val winner: String
        val winColour: Int

        if(game.checkIfPlayerOnesTurn())
        {
            winner = gameActivity.baseContext.resources.getString(R.string.p1win)
            winColour = colour1
        }
        else
        {
            winner = gameActivity.baseContext.resources.getString(R.string.p2win)
            winColour = colour2
        }

        with(gameActivity) {
            displayMessage(winner, winColour, true)
            lockGameControls()
            stopTheTimer()
        }

    }

    fun startNewGame() {
        with(gameActivity) {
            viewModelStore.clear()
            recreate()
        }
    }

    fun setViewColour(v: View, occupation: GridItem.Companion.Occupation) {
        if(occupation == GridItem.Companion.Occupation.PLAYER1)
            v.setBackgroundColor(colour1)
        else if(occupation == GridItem.Companion.Occupation.PLAYER2)
            v.setBackgroundColor(colour2)
        else
            v.setBackgroundColor(defaultColor)
    }

    fun getMinutes() : Int { return game.getMinutes() }
    fun setMinutes(minutes: Int) { game.setMinutes(minutes) }

    fun getSeconds() : Int { return game.getSeconds() }
    fun setSeconds(seconds: Int) { game.setSeconds(seconds) }

    fun isGameEnded() : Boolean { return game.isGameEnded() }
}