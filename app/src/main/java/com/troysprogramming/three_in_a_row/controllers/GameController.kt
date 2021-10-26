package com.troysprogramming.three_in_a_row.controllers

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

    fun generateGrid(gameView: GameActivity) {
        // TODO: get grid size from shared preferences
        val gridSize: Int = 4

        // create a new game object and initialise the 2D grid of GridItems
        Game.startNewGame(gridSize)

        // pass grid size to view to display the grid
        gameView.renderGrid(gridSize)
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
                    Color.RED
                else
                    Color.BLUE
            )

            // check for three in a row
            var isGameOver : Boolean = Game.getGame().checkForThree(x, y)

            // swap turns
            Game.getGame().swapTurns()

            // if the game is over, show the winner
            if(isGameOver)
            {
                val winner : String = if(Game.getGame().checkIfPlayerOnesTurn())
                    gameActivity.baseContext.resources.getString(R.string.p1win)
                else
                    gameActivity.baseContext.resources.getString(R.string.p2win)

                with(gameActivity) {
                    displayMessage(winner, true)
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
                .getString(R.string.invalidmove), false)
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
                Color.RED
            else
                Color.BLUE
        )
    }
}