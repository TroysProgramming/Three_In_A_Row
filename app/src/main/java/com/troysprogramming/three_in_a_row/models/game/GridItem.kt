package com.troysprogramming.three_in_a_row.models.game

class GridItem {
    companion object {
        enum class Occupation { NONE, PLAYER1, PLAYER2 }
    }

    private var occupation : Occupation = Occupation.NONE

    fun getOccupation() : Occupation { return occupation }

    fun setOccupation(occupation: Occupation) {
        this.occupation = occupation
    }
}