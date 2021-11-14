package com.troysprogramming.three_in_a_row.models.game

import retrofit2.Call
import retrofit2.http.*

interface ServerScore {

    @POST("Score")
    fun createScore(@Body score: HighScore) : Call<HighScore>

    @GET("Score")
    fun getAllScores() : Call<List<HighScore>>

    @GET("Score/{id}")
    fun getOneScore(@Path("id") id: Int) : Call<HighScore>

    @PUT("Score/{id}")
    fun updateScore(@Path("id") id: Int, @Body score: HighScore) : Call<Unit>

    @DELETE("Score/{id}")
    fun deleteScore(@Path("id") id: Int) : Call<HighScore>
}