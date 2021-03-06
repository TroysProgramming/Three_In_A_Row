package com.troysprogramming.three_in_a_row.views

import android.content.Intent
import android.os.Bundle
import android.view.View.GONE
import android.view.View.INVISIBLE
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import com.troysprogramming.three_in_a_row.R
import com.troysprogramming.three_in_a_row.models.User
import com.troysprogramming.three_in_a_row.models.database.SQLiteService

class WelcomeActivity : AppCompatActivity() {

    private lateinit var btnStart : Button
    private lateinit var btnSettings : Button
    private lateinit var btnHighScores : Button
    private lateinit var btnLogin : Button
    private lateinit var txtLogin : TextView

    private lateinit var constrButtons : ConstraintLayout

    private var loggedIn : Boolean = false

    override fun onCreate(sis: Bundle?) {
        super.onCreate(sis)
        setContentView(R.layout.layout_welcome)

        SQLiteService.createNewInstance(applicationContext)

        constrButtons = findViewById(R.id.constr_master)

        btnStart = findViewById(R.id.btn_start)
        btnSettings = findViewById(R.id.btn_settings)
        btnHighScores = findViewById(R.id.btn_highscore)
        btnLogin = findViewById(R.id.btn_login)
        txtLogin = findViewById(R.id.txt_login)

        btnStart.setOnClickListener {
            startActivity(Intent(this, GameActivity::class.java))
        }
        btnSettings.setOnClickListener {
            startActivity(Intent(this, SettingsActivity::class.java))
        }

        btnHighScores.setOnClickListener {
            startActivity(Intent(this, FragmentActivity::class.java))
        }

        btnLogin.setOnClickListener { btnLogin() }
    }

    override fun onStart() {
        super.onStart()

        if(User.getUser().getID() != 0)
        {
            loggedIn = true
            txtLogin.text = String.format(resources.getText(R.string.loggedinas) as String,
                User.getUser().getUsername())
            btnLogin.text = resources.getText(R.string.logout)
        }
    }

    private fun btnLogin() {
        if(loggedIn) {
            User.logout()
            loggedIn = false
            recreate()
        }
        else {
            startActivity(Intent(this, UserLoginSignupActivity::class.java))
        }
    }
}