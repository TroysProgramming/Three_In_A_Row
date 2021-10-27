package com.troysprogramming.three_in_a_row.views

import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.gridlayout.widget.GridLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.get
import com.troysprogramming.three_in_a_row.R
import com.troysprogramming.three_in_a_row.controllers.GameController
import java.util.*
import kotlin.collections.ArrayList

class GameActivity : AppCompatActivity() {

    private lateinit var controller : GameController
    private lateinit var grid : GridLayout
    private lateinit var gridItems : ArrayList<ArrayList<View>>
    private lateinit var currentTurnColour : View
    private lateinit var txtMessage : TextView
    private lateinit var txtTimerSeconds : TextView
    private lateinit var txtTimerMinutes : TextView
    private lateinit var btnRestart : Button
    private var seconds : Int = 0
    private var minutes : Int = 0
    private lateinit var timer : CountDownTimer

    override fun onCreate(sis: Bundle?) {
        super.onCreate(sis)
        setContentView(R.layout.layout_game)

        controller = GameController(this)

        grid = findViewById(R.id.grid_game)
        val currentTurnView : View = findViewById(R.id.comp_current_turn)
        txtTimerSeconds = findViewById(R.id.txt_timer_seconds)
        txtTimerMinutes = findViewById(R.id.txt_timer_minutes)
        currentTurnColour = currentTurnView.findViewById(R.id.view_colour)
        txtMessage = findViewById(R.id.txt_message)
        btnRestart = findViewById(R.id.btn_restart)
        btnRestart.setOnClickListener { recreate() }

        controller.setPlayerColours()

        controller.generateGrid()

        fireUpTheTimer()

        controller.showCurrentTurn(currentTurnColour)
    }

    fun renderGrid(gridSize: Int) {

        grid.rowCount = gridSize
        grid.columnCount = gridSize

        gridItems = ArrayList()

        for(i in 0 until gridSize)
        {
            gridItems.add(ArrayList())

            for(j in 0 until gridSize)
            {
                layoutInflater.inflate(R.layout.comp_grid_item, grid, true)
                val gridToFill = grid[i * gridSize + j]
                gridToFill.tag = i * gridSize + j
                gridToFill.setOnClickListener { v: View -> onGridItemClick(v) }
                gridItems[i].add(gridToFill)
            }
        }
    }

    private fun onGridItemClick(v: View) {
        val search = controller.findIDIn2DArray(v.tag as Int, gridItems)

        if(search[0] != -1)
            controller.onGridItemClick(v.findViewById(R.id.view_colour), search[0], search[1],
                currentTurnColour)
        else
            Toast.makeText(this, "Not found.", Toast.LENGTH_SHORT).show()
    }

    fun lockGameControls() {
        for(i in gridItems.indices)
        {
            for(j in gridItems[i])
            {
                j.setOnClickListener(null)
            }
        }
    }

    fun displayMessage(message: String, color: Int, permanent: Boolean) {

        txtMessage.text = message

        txtMessage.setTextColor(color)

        if(!permanent) {
            Timer().schedule(object: TimerTask() {
                override fun run() { runOnUiThread { txtMessage.text = "" } }
            }, 4000)
        }
    }

    private fun fireUpTheTimer() {
        timer = object: CountDownTimer(Long.MAX_VALUE, 1000) {
            override fun onTick(p0: Long) {
                seconds++
                if(seconds.mod(60) == 0) {
                    seconds = 0
                    minutes++
                }

                runOnUiThread {
                    txtTimerSeconds.text = if(seconds < 10) "0$seconds" else seconds.toString()
                    txtTimerMinutes.text = if(minutes < 10) "0$minutes" else minutes.toString()
                }
            }

            override fun onFinish() { }
        }.start()
    }

    fun stopTheTimer() {
        timer.cancel()
    }
}
