package com.troysprogramming.three_in_a_row.viewmodels.vmfactory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.troysprogramming.three_in_a_row.viewmodels.GameViewModel

class GameVMFactory(colour1: Int, colour2: Int, gridSize: String) : ViewModelProvider.Factory {
    private var col1: Int = colour1
    private var col2: Int = colour2
    private var size : String = "4 x 4"

    init {
        col1 = colour1
        col2 = colour2
        size = gridSize
    }

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return GameViewModel(col1, col2, size) as T
    }
}