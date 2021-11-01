package com.troysprogramming.three_in_a_row.models.game;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey
import androidx.room.PrimaryKey;
import com.troysprogramming.three_in_a_row.models.User

@Entity(tableName = "score")
class HighScore(id: Int, userId: Int, userName: String, time: String, date: String, gridSize: String) {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Int = id

    @ColumnInfo(name = "userID")
    var userId: Int = userId

    @ColumnInfo(name = "userName")
    private var userName: String = userName

    @ColumnInfo(name = "time")
    private var time: String = time

    @ColumnInfo(name = "date")
    private var date: String = date

    @ColumnInfo(name = "gridSize")
    private var gridSize: String = gridSize

    fun getID(): Int { return id }
    fun getUserID(): Int { return userId }
    fun getUserName(): String { return userName }
    fun getTime(): String { return time }
    fun getDate(): String { return date }
    fun getGridSize(): String { return gridSize }
}