package com.troysprogramming.three_in_a_row.views

import android.content.Context
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.gridlayout.widget.GridLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.get
import androidx.lifecycle.ViewModelProvider
import com.troysprogramming.three_in_a_row.R
import com.troysprogramming.three_in_a_row.models.game.GameVMFactory
import com.troysprogramming.three_in_a_row.models.game.GridItem
import com.troysprogramming.three_in_a_row.viewmodels.GameViewModel
import kotlin.collections.ArrayList

class GameActivity : AppCompatActivity() {
    private lateinit var sharedPref : SharedPreferences
    private lateinit var grid : GridLayout
    private lateinit var gridItems : ArrayList<ArrayList<View>>
    private lateinit var currentTurnColour : View
    private lateinit var txtMessage : TextView
    private lateinit var txtTimerSeconds : TextView
    private lateinit var txtTimerMinutes : TextView
    private lateinit var btnRestart : Button
    private lateinit var gameVM : GameViewModel

    override fun onCreate(sis: Bundle?) {
        sharedPref = this.getSharedPreferences("3inarow_settings.xml", Context.MODE_PRIVATE)

        super.onCreate(sis)
        setContentView(R.layout.layout_game)

        grid = findViewById(R.id.grid_game)

        val currentTurnView : View = findViewById(R.id.comp_current_turn)
        txtTimerSeconds = findViewById(R.id.txt_timer_seconds)
        txtTimerMinutes = findViewById(R.id.txt_timer_minutes)
        currentTurnColour = currentTurnView.findViewById(R.id.view_colour)
        txtMessage = findViewById(R.id.txt_message)
        btnRestart = findViewById(R.id.btn_restart)
        btnRestart.setOnClickListener { startNewGame() }

        initGameVM()

        gameVM.getMinutes().observe(this, { minutes ->
            txtTimerMinutes.text = minutes
        })
        gameVM.getSeconds().observe(this, { seconds ->
            txtTimerSeconds.text = seconds
        })
        gameVM.getMessage().observe(this, { message ->
            txtMessage.text = message
        })
        gameVM.getMessageColour().observe(this, { msgCol ->
            txtMessage.setTextColor(msgCol)
        })
        gameVM.getGameEnded().observe(this, { gameEnded ->
            if(gameEnded)
                lockGameControls()
        })
        gameVM.isPlayerOnesTurn().observe(this, { p1turn ->
            currentTurnColour.setBackgroundColor(
                if(p1turn)
                    gameVM.getColour1()
                else
                    gameVM.getColour2()
            )
        })
    }

    private fun initGameVM() {
        val colour1: Int = sharedPref.getInt("colour_1", Color.RED)
        val colour2: Int = sharedPref.getInt("colour_2", Color.BLUE)
        val gridSizeStr : String? = sharedPref.getString("grid_size", "4 x 4")
        val gridSize = gridSizeStr!![0].digitToInt()
        gameVM = ViewModelProvider(this, GameVMFactory(colour1, colour2, gridSizeStr))
            .get(GameViewModel::class.java)
        renderGrid(gridSize)
    }

    private fun renderGrid(gridSize: Int) {

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
                gameVM.getTile(i, j).observe(this, { tile ->
                    gridToFill.findViewById<View>(R.id.view_colour).setBackgroundColor(
                        when(tile.getOccupation()) {
                            GridItem.Companion.Occupation.PLAYER1 -> gameVM.getColour1()
                            GridItem.Companion.Occupation.PLAYER2 -> gameVM.getColour2()
                            else -> gameVM.getDefaultColour()
                        }
                    )
                })
            }
        }
    }

    private fun onGridItemClick(v: View) {
        val search = gameVM.findIDIn2DArray(v.tag as Int, gridItems)

        if(search[0] != -1)
            gameVM.onGridItemClick(search[0], search[1])
        else
            Toast.makeText(this, "Not found.", Toast.LENGTH_SHORT).show()
    }

    private fun lockGameControls() {
        for(i in gridItems.indices) {
            for(j in gridItems[i]) {
                j.setOnClickListener(null)
            }
        }
    }

    private fun startNewGame() {
        viewModelStore.clear()
        recreate()
    }
}
