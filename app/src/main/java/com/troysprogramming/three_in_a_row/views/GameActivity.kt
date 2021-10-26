package com.troysprogramming.three_in_a_row.views

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.gridlayout.widget.GridLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.get
import com.troysprogramming.three_in_a_row.R
import com.troysprogramming.three_in_a_row.controllers.GameController

class GameActivity : AppCompatActivity() {

    private lateinit var controller : GameController
    private lateinit var grid : GridLayout
    private lateinit var gridItems : ArrayList<ArrayList<View>>


    override fun onCreate(sis: Bundle?) {
        super.onCreate(sis)
        setContentView(R.layout.layout_game)

        controller = GameController()

        grid = findViewById(R.id.grid_game)

        controller.generateGrid(this)
    }

    fun renderGrid(x: Int, y: Int) {

        grid.rowCount = x
        grid.columnCount = y

        gridItems = ArrayList()

        for(i in 0 until x)
        {
            gridItems.add(ArrayList())

            for(j in 0 until y)
            {
                layoutInflater.inflate(R.layout.comp_grid_item, grid, true)
                val gridToFill = grid[i * x + j]
                gridToFill.tag = i * x + j
                gridToFill.setOnClickListener { v: View -> onGridItemClick(v) }
                gridItems[i].add(gridToFill)
            }
        }
    }

    private fun onGridItemClick(v: View) {
        val search = controller.findIDIn2DArray(v.tag as Int, gridItems)

        if(search[0] != -1)
            controller.onGridItemClick(v.findViewById(R.id.view_colour), search[0], search[1])
        else
            Toast.makeText(this, "Not found.", Toast.LENGTH_SHORT).show()
    }
}