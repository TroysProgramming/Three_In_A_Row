package com.troysprogramming.three_in_a_row.views

import android.content.Intent
import android.os.Bundle
import android.view.View.GONE
import android.view.View.INVISIBLE
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import com.troysprogramming.three_in_a_row.R
import com.troysprogramming.three_in_a_row.models.database.SQLiteService

class WelcomeActivity : AppCompatActivity() {

    private lateinit var btnStart : Button
    private lateinit var btnSettings : Button
    private lateinit var btnHighScores : Button

    private lateinit var constrButtons : ConstraintLayout
    private lateinit var highScores : Fragment

    override fun onCreate(sis: Bundle?) {
        super.onCreate(sis)
        setContentView(R.layout.layout_welcome)

        SQLiteService.createNewInstance(applicationContext)
        highScores = HighScoreFragment()

        if (sis == null) {
            supportFragmentManager.commit {
                setReorderingAllowed(true)
                add(R.id.frag_highscores, highScores)
                hide(highScores)
            }
        }

        constrButtons = findViewById(R.id.constr_master)

        btnStart = findViewById(R.id.btn_start)
        btnSettings = findViewById(R.id.btn_settings)
        btnHighScores = findViewById(R.id.btn_highscore)

        btnStart.setOnClickListener {
            startActivity(Intent(this, GameActivity::class.java))
        }
        btnSettings.setOnClickListener {
            startActivity(Intent(this, SettingsActivity::class.java))
        }

        btnHighScores.setOnClickListener {
            supportFragmentManager.commit {
                show(highScores)
            }
        }
    }

    /*
    override fun onBackPressed() {
        if(!highScores.isHidden) {
            supportFragmentManager.commit {
                hide(highScores)
            }
        }
        else
            super.onBackPressed()
    }

    */
}