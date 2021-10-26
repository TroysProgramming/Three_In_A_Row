package com.troysprogramming.three_in_a_row.models

class Game(x: Int, y: Int) {

    private var grid : Array<Array<GridItem>> = Array(x) { Array(y) { GridItem() } }
    private var isPlayerOneTurn : Boolean = false

    companion object {
        private var game : Game? = null

        fun startNewGame(x: Int, y: Int) {
            game = Game(x, y)
        }

        fun getGame() : Game { return game!! }
    }

    fun checkValidMove(x: Int, y: Int) : Boolean {
        if(isPlayerOneTurn && grid[x][y].getOccupation() == GridItem.Companion.Occupation.PLAYER1)
            return false
        else if(!isPlayerOneTurn && grid[x][y].getOccupation() == GridItem.Companion.Occupation.PLAYER2)
            return false
        else
            return true

    }

    fun changeOccupationToCurrentPlayer(x: Int, y: Int) : Unit {
        grid[x][y].setOccupation(
            if(isPlayerOneTurn)
                GridItem.Companion.Occupation.PLAYER1
            else
                GridItem.Companion.Occupation.PLAYER2)
    }

    fun getOccupation(x: Int, y: Int) : GridItem.Companion.Occupation {
        return grid[x][y].getOccupation()
    }

    fun swapTurns() : Unit {
        isPlayerOneTurn = !isPlayerOneTurn
    }
}