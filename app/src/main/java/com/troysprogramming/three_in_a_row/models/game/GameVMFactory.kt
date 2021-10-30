package com.troysprogramming.three_in_a_row.models.game

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class GameVMFactory(gridSize: Int) : ViewModelProvider.Factory {
    private var size : Int = 4

    init {
        size = gridSize
    }

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return Game(size) as T
    }
}