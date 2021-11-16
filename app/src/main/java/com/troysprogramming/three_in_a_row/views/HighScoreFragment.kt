package com.troysprogramming.three_in_a_row.views

import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.troysprogramming.three_in_a_row.R
import com.troysprogramming.three_in_a_row.models.User
import com.troysprogramming.three_in_a_row.viewmodels.HighScoreViewModel

class HighScoreFragment : Fragment(R.layout.layout_highscores) {

    private lateinit var llvScores : LinearLayout
    private lateinit var btnSwapScores: ImageButton
    private lateinit var highScoreVM: HighScoreViewModel

    override fun onStart() {
        super.onStart()

        highScoreVM = ViewModelProvider(this).get(HighScoreViewModel::class.java)

        with(requireView()) {
            llvScores = findViewById(R.id.llv_scores)
            btnSwapScores = findViewById(R.id.btn_swapscores)
        }

        btnSwapScores.setOnClickListener { highScoreVM.swapScores() }

        highScoreVM.getScores().observe(this, { scores ->
            for (i in 0 until llvScores.childCount) {

                val constrScore : ConstraintLayout = llvScores.getChildAt(i) as ConstraintLayout
                val txtPosition : TextView = constrScore.findViewById(R.id.txt_position)
                val txtTime : TextView = constrScore.findViewById(R.id.txt_time)
                val txtGrid : TextView = constrScore.findViewById(R.id.txt_grid)
                val txtDate : TextView = constrScore.findViewById(R.id.txt_date)
                val txtUser : TextView = constrScore.findViewById(R.id.txt_user)

                txtPosition.text = (i + 1).toString()

                if(i < highScoreVM.getScoreListLength())
                {
                    txtTime.text = scores[i].getTime()
                    txtGrid.text = scores[i].getGridSize()
                    txtDate.text = scores[i].getDate()
                    txtUser.text = scores[i].getUserName()
                }
                else
                {
                    txtTime.text = ""
                    txtGrid.text = ""
                    txtDate.text = ""
                    txtUser.text = ""
                }
            }
        })

        highScoreVM.getIsUserScores().observe(this, { isUserScore ->
            btnSwapScores.setImageResource(
                if(isUserScore)
                    android.R.drawable.ic_menu_mapmode
                else
                    android.R.drawable.ic_menu_myplaces
            )
        })
    }
}