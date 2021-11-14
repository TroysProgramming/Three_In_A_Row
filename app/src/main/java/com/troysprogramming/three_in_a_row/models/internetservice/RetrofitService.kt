package com.troysprogramming.three_in_a_row.models.internetservice

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import kotlin.reflect.KClass

abstract class RetrofitService(url: String, opClass: KClass<*>) {

    protected var retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(url)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    init {
        retrofit.create(opClass.java)
    }

}