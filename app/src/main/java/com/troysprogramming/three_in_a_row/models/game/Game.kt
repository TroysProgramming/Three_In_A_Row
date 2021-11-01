package com.troysprogramming.three_in_a_row.models.game

import android.os.CountDownTimer
import androidx.lifecycle.ViewModel

open class Game(gridSize: Int) : ViewModel() {

    private var grid : Array<Array<GridItem>> = Array(gridSize) { Array(gridSize) { GridItem() } }
    private var isPlayerOneTurn : Boolean = true
    private var lastItemClicked : GridItem? = null
    private var minutes : Int = 0
    private var seconds : Int = 0

    private var gameEnded = false

    fun checkValidMove(x: Int, y: Int) : Boolean {
        return if(isPlayerOneTurn && grid[x][y].getOccupation() == GridItem.Companion.Occupation.PLAYER1)
            false
        else if(!isPlayerOneTurn && grid[x][y].getOccupation() == GridItem.Companion.Occupation.PLAYER2)
            false
        else
            lastItemClicked != grid[x][y]
    }

    fun changeOccupationToCurrentPlayer(x: Int, y: Int) : Unit {
        grid[x][y].setOccupation(
            if(isPlayerOneTurn)
                GridItem.Companion.Occupation.PLAYER1
            else
                GridItem.Companion.Occupation.PLAYER2)

        lastItemClicked = grid[x][y]
    }

    fun getOccupation(x: Int, y: Int) : GridItem.Companion.Occupation {
        return grid[x][y].getOccupation()
    }

    fun swapTurns() : Unit {
        isPlayerOneTurn = !isPlayerOneTurn
    }

    fun checkIfPlayerOnesTurn() : Boolean {
        return isPlayerOneTurn
    }

    // CHECK FOR WIN ALGORITHM
    fun checkForThree(x: Int, y: Int) : Boolean {
        val targetCell = arrayOf(x, y)
        return (checkCells(targetCell, arrayOf(x, y-1), arrayOf(x, y-2)) ||
               checkCells(targetCell, arrayOf(x+1, y), arrayOf(x+2, y)) ||
               checkCells(targetCell, arrayOf(x, y+1), arrayOf(x, y+2)) ||
               checkCells(targetCell, arrayOf(x-1, y), arrayOf(x-2, y)) ||
               checkCells(targetCell, arrayOf(x-1, y), arrayOf(x+1, y)) ||
               checkCells(targetCell, arrayOf(x, y-1), arrayOf(x, y+1)))
    }

    // CHECK THREE CELLS IN ONE ROW AROUND THE CHANGED GRID ITEM
    private fun checkCells(c1: Array<Int>, c2: Array<Int>, c3: Array<Int>) : Boolean {
        // check if the cells exist
        if(!(c1[0] in grid.indices && c1[1] in grid[0].indices) ||
           !(c2[0] in grid.indices && c2[1] in grid[0].indices) ||
           !(c3[0] in grid.indices && c3[1] in grid[0].indices))
               return false
        // check if three tiles in a row match
        else if(grid[c1[0]][c1[1]].getOccupation() == grid[c2[0]][c2[1]].getOccupation() &&
                grid[c2[0]][c2[1]].getOccupation() == grid[c3[0]][c3[1]].getOccupation())
        {
            // return true if the three matching tiles belong to a player
            return grid[c1[0]][c1[1]].getOccupation() != GridItem.Companion.Occupation.NONE
        }
        else
            return false
    }

    fun getMinutes() : Int { return minutes }
    fun setMinutes(minutes: Int) { this.minutes = minutes }

    fun getSeconds() : Int { return seconds }
    fun setSeconds(seconds: Int) { this.seconds = seconds }

    fun setGameEnded(gameEnded: Boolean) { this.gameEnded = gameEnded }
    fun isGameEnded() : Boolean { return gameEnded }
}