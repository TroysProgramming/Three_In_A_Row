package com.troysprogramming.three_in_a_row.controllers

import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.util.Log
import android.view.View
import com.troysprogramming.three_in_a_row.models.Game
import com.troysprogramming.three_in_a_row.models.GridItem
import com.troysprogramming.three_in_a_row.views.GameActivity

class GameController
{
    fun generateGrid(gameView: GameActivity) {
        // TODO: get grid size from shared preferences
        val gridSizeX: Int = 5
        val gridSizeY: Int = 5

        // create a new game object and initialise the 2D grid of GridItems
        Game.startNewGame(gridSizeX, gridSizeY)

        // pass grid size to view to display the grid
        gameView.renderGrid(gridSizeX, gridSizeY)
    }

    fun onGridItemClick(v: View, x: Int, y: Int) {
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
            v.setBackgroundColor(
                if(occupation == GridItem.Companion.Occupation.PLAYER1)
                    Color.RED
                else
                    Color.BLUE
            )

            // swap turns
            Game.getGame().swapTurns()
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
}