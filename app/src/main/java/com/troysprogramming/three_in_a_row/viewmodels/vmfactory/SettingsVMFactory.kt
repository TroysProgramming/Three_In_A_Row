package com.troysprogramming.three_in_a_row.viewmodels.vmfactory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.troysprogramming.three_in_a_row.viewmodels.SettingsViewModel

class SettingsVMFactory(
    colour1: Int,
    colour2: Int,
    gridSize: String,
    gridSizeList: Array<String>
) : ViewModelProvider.Factory {
    private var col1: Int = colour1
    private var col2: Int = colour2
    private var size: String = gridSize
    private var gridSizes: Array<String> = gridSizeList

    override fun <T: ViewModel?> create(modelClass: Class<T>): T {
        return SettingsViewModel(col1, col2, size, gridSizes) as T
    }
}