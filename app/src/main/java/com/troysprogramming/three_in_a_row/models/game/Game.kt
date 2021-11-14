package com.troysprogramming.three_in_a_row.models.game

import android.graphics.Color
import android.os.CountDownTimer
import com.troysprogramming.three_in_a_row.models.User
import com.troysprogramming.three_in_a_row.models.database.SQLiteService
import java.util.*

data class Game(val colour1: Int, val colour2: Int, val gridSize: Int) {

    private var grid : Array<Array<GridItem>> = Array(gridSize) { Array(gridSize) { GridItem() } }
    private var isPlayerOneTurn : Boolean = true
    private var lastItemClicked : GridItem? = null
    private var minutes : Int = 0
    private var seconds : Int = 0
    private var timer: CountDownTimer? = null
    private var message: String = ""
    private var p1Colour: Int = colour1
    private var p2Colour: Int = colour2
    private val messageErrorColour: Int = Color.RED
    private var messageCurrentColour: Int = 0
    private var timeListener: GameTimeListener? = null

    init {
        fireUpTheTimer()
        allocateFourTiles()
    }

    private var gameEnded = false

    fun addTimeListener(listener: GameTimeListener) {
        this.timeListener = listener
    }

    private fun checkValidMove(x: Int, y: Int) : Boolean {
        return if(isPlayerOneTurn && grid[x][y].getOccupation() == GridItem.Companion.Occupation.PLAYER1)
            false
        else if(!isPlayerOneTurn && grid[x][y].getOccupation() == GridItem.Companion.Occupation.PLAYER2)
            false
        else
            lastItemClicked != grid[x][y]
    }

    private fun changeOccupationToCurrentPlayer(x: Int, y: Int) {
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

    private fun swapTurns() {
        isPlayerOneTurn = !isPlayerOneTurn
    }

    fun checkIfPlayerOnesTurn() : Boolean {
        return isPlayerOneTurn
    }

    fun onGridItemClick(x: Int, y: Int) {
        // notify the Game instance that a grid item in the passed co-ordinates has been touched
        val isValidMove : Boolean = checkValidMove(x, y)

        // if the move is valid,
        if(isValidMove)
        {
            // set the grid item's ownership to the current player
            changeOccupationToCurrentPlayer(x, y)

            // check for three in a row
            val isGameOver : Boolean = checkForThree(x, y)

            // swap turns
            swapTurns()

            // if the game is over, show the winner
            if(isGameOver)
            {
                SQLiteService.getInstance().createScore(
                    HighScore(
                        SQLiteService.getInstance().getHighestScoreID() + 1,
                        User.getUser().getID(),
                        User.getUser().getUsername(),
                        getTime(),
                        "01/01/2021",
                        "${grid.size} x ${grid.size}"
                    )
                )

                endGame()
            }
        }
        else
        {
            // if the move is not valid, present an error to the user.
            handleInvalidMove()
        }
    }

    private fun handleInvalidMove() {
        displayMessage("Invalid move.", messageErrorColour, false)
    }

    private fun endGame() {
        gameEnded = true
        timer!!.cancel()

        val winner: String
        val winColour: Int

        if(checkIfPlayerOnesTurn())
        {
            winner = "Player 1 Wins!"
            winColour = p1Colour
        }
        else
        {
            winner = "Player 2 Wins!"
            winColour = p2Colour
        }

        showWinner(winner, winColour)
    }

    private fun showWinner(winner: String, winColour: Int) {
        displayMessage(winner, winColour, true)
    }

    // CHECK FOR WIN ALGORITHM
    private fun checkForThree(x: Int, y: Int) : Boolean {
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

    private fun fireUpTheTimer() {
        timer = object: CountDownTimer(Long.MAX_VALUE, 1000) {
            override fun onTick(p0: Long) {
                if(timer != null)
                {
                    seconds++
                    if(seconds.mod(60) == 0) {
                        seconds = 0
                        minutes++
                    }
                }

                timeListener?.onTick()
            }

            override fun onFinish() { }
        }.start()
    }

    private fun allocateFourTiles() {
        var startingTiles: Array<Array<Int>> = arrayOf(
            arrayOf(-1, -1),
            arrayOf(-1, -1),
            arrayOf(-1, -1),
            arrayOf(-1, -1)
        )

        var allocations: Int = 0

        while(allocations < 4) {
            val num1 = Random().nextInt(gridSize)
            val num2 = Random().nextInt(gridSize)

            var isValid = true

            for(i in startingTiles.indices) {
                if(num1 == startingTiles[i][0] && num2 == startingTiles[i][1]) {
                    isValid = false
                    break
                }
            }

            if(!isValid)
                continue

            startingTiles[allocations][0] = num1
            startingTiles[allocations][1] = num2

            allocations++
        }

        for(i in startingTiles) {
            onGridItemClick(i[0], i[1])
        }
    }

    fun getMinutes() : String { return formatTimeToString(minutes) }
    fun getSeconds() : String { return formatTimeToString(seconds) }
    private fun formatTimeToString(time: Int): String {
        return if(time < 10) "0${time}" else "$time"
    }

    private fun getTime(): String {
        return "${formatTimeToString(minutes)}:${formatTimeToString(seconds)}"
    }

    fun getMessage(): String { return message }
    fun getCurrentColour(): Int { return messageCurrentColour }
    fun getGameEnded(): Boolean { return gameEnded }
    fun getBoard(): Array<Array<GridItem>> { return grid }
    private fun displayMessage(msg: String, colour: Int, permanent: Boolean) {
        message = msg
        messageCurrentColour = colour

        if(!permanent) {
            Timer().schedule(object: TimerTask() {
                override fun run() {
                    clearMessage()
                    timeListener?.onMessageClear()
                }
            }, 4000)
        }
    }
    fun clearMessage() {
        message = ""
    }

    interface GameTimeListener {
        fun onTick()
        fun onMessageClear()
    }
}