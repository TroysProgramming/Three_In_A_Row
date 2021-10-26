package com.troysprogramming.three_in_a_row.views

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.troysprogramming.three_in_a_row.R

class WelcomeActivity : AppCompatActivity() {

    private lateinit var btnStart : Button
    private lateinit var btnSettings : Button
    private lateinit var btnHighScores : Button


    override fun onCreate(sis: Bundle?) {
        super.onCreate(sis)
        setContentView(R.layout.layout_welcome)

        btnStart = findViewById(R.id.btn_start)
        btnSettings = findViewById(R.id.btn_settings)
        btnHighScores = findViewById(R.id.btn_highscore)

        btnStart.setOnClickListener {
            startActivity(Intent(this, GameActivity::class.java))
        }
    }
}