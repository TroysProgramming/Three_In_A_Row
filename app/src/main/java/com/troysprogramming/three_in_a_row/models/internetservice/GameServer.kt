package com.troysprogramming.three_in_a_row.models.internetservice

import com.troysprogramming.three_in_a_row.models.game.ServerScore

class GameServer : RetrofitService("http://10.0.0.9:5000/api/", ServerScore::class) {

    private var service: ServerScore = retrofit.create(ServerScore::class.java)
}