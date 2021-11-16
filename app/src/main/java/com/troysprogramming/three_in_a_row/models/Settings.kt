package com.troysprogramming.three_in_a_row.models

class Settings(
    private var colour1: Int,
    private var colour2: Int,
    private var gridSize: String,
    private val gridSizeList: Array<String>
)
{
    fun getColour1(): Int { return colour1 }
    fun applyColour1(colour: Int) { colour1 = colour }
    fun getColour2(): Int { return colour2 }
    fun applyColour2(colour: Int) { colour2 = colour }
    fun getGridSize(): String { return gridSize }
    fun applyGridSize(gridSize: String) { this.gridSize = gridSize }
    fun getGridSizeList(): Array<String> { return gridSizeList }
}