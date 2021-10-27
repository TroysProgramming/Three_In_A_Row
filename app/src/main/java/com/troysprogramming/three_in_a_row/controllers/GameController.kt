package com.troysprogramming.three_in_a_row.controllers

import android.content.Context
import android.content.SharedPreferences
import android.graphics.Color
import android.view.View
import android.widget.Toast
import android.widget.Toast.LENGTH_LONG
import android.widget.Toast.LENGTH_SHORT
import com.troysprogramming.three_in_a_row.R
import com.troysprogramming.three_in_a_row.models.Game
import com.troysprogramming.three_in_a_row.models.GridItem
import com.troysprogramming.three_in_a_row.views.GameActivity

class GameController(activity: GameActivity)
{
    private var gameActivity: GameActivity = activity
    private val sharedPref : SharedPreferences =
        gameActivity.getSharedPreferences("3inarow_settings.xml", Context.MODE_PRIVATE)

    private var colour1 : Int = 0
    private var colour2 : Int = 0

    fun setPlayerColours() {
        colour1 = sharedPref.getInt("colour_1", Color.RED)
        colour2 = sharedPref.getInt("colour_2", Color.BLUE)
    }

    fun generateGrid() {

        val gridSizeStr: String? = sharedPref.getString("grid_size", "4 x 4")
        val gridSize: Int = gridSizeStr?.get(0)!!.digitToInt()

        // create a new game object and initialise the 2D grid of GridItems
        Game.startNewGame(gridSize)

        // pass grid size to view to display the grid
        gameActivity.renderGrid(gridSize)
    }

    fun onGridItemClick(clickedView: View, x: Int, y: Int, currentTurnView: View) {
        // notify the Game instance that a grid item in the passed co-ordinates has been touched
        val isValidMove : Boolean = Game.getGame().checkValidMove(x, y)

        // if the move is valid,
        if(isValidMove)
        {
            // set the grid item's ownership to the current player
            Game.getGame().changeOccupationToCurrentPlayer(x, y)

            // get the current player's turn
            val occupation : GridItem.Companion.Occupation = Game.getGame().getOccupation(x, y)

            // set the tapped view to their colour
            clickedView.setBackgroundColor(
                if(occupation == GridItem.Companion.Occupation.PLAYER1)
                    colour1
                else
                    colour2
            )

            // check for three in a row
            var isGameOver : Boolean = Game.getGame().checkForThree(x, y)

            // swap turns
            Game.getGame().swapTurns()

            // if the game is over, show the winner
            if(isGameOver)
            {
                val winner: String
                val winColour: Int

                if(Game.getGame().checkIfPlayerOnesTurn())
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
            if(Game.getGame().checkIfPlayerOnesTurn())
                colour1
            else
                colour2
        )
    }
}