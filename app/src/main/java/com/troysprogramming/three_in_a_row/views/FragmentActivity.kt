package com.troysprogramming.three_in_a_row.views

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import com.troysprogramming.three_in_a_row.R

class FragmentActivity : AppCompatActivity() {

    private lateinit var highScores : Fragment

    override fun onCreate(sis: Bundle?) {
        super.onCreate(sis)
        setContentView(R.layout.layout_fragment_view)
        highScores = HighScoreFragment()

        supportFragmentManager.commit {
            setReorderingAllowed(true)
            add(R.id.frag_master, highScores)
        }
    }
}