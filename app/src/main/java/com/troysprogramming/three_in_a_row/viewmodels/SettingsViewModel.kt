package com.troysprogramming.three_in_a_row.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.troysprogramming.three_in_a_row.models.Settings

class SettingsViewModel(
    colour1: Int,
    colour2: Int,
    gridSize: String,
    gridSizeList: Array<String>
) : ViewModel() {

    private var settings: Settings = Settings(colour1, colour2, gridSize, gridSizeList)

    private var setCol1: MutableLiveData<Int> = MutableLiveData<Int>()
    private var setCol2: MutableLiveData<Int> = MutableLiveData<Int>()
    private var setGridSize: MutableLiveData<String> = MutableLiveData<String>()

    init {
        setCol1.value = settings.getColour1()
        setCol2.value = settings.getColour2()
        setGridSize.value = settings.getGridSize()
    }

    fun applyColour1(colour: Int) {
        settings.applyColour1(colour)
        setCol1.value = colour
    }

    fun applyColour2(colour: Int) {
        settings.applyColour2(colour)
        setCol2.value = colour
    }

    fun onGridSizeSelected(gridSize: String) {
        settings.applyGridSize(gridSize)
        setGridSize.value = gridSize
    }

    fun getColour1(): MutableLiveData<Int> { return setCol1 }
    fun getColour2(): MutableLiveData<Int> { return setCol2 }
    fun getGridSize(): MutableLiveData<String> { return setGridSize }
    fun getGridSizeList(): Array<String> { return settings.getGridSizeList() }
}