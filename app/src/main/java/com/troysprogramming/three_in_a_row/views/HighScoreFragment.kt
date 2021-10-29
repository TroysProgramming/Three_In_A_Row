package com.troysprogramming.three_in_a_row.views

import android.os.Bundle
import android.widget.LinearLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import com.troysprogramming.three_in_a_row.R
import com.troysprogramming.three_in_a_row.models.database.SQLiteService
import com.troysprogramming.three_in_a_row.models.game.HighScore

class HighScoreFragment : Fragment(R.layout.layout_highscores) {

    private lateinit var llvScores : LinearLayout
    private lateinit var scores : ArrayList<HighScore>

    override fun onStart() {
        super.onStart()

        llvScores = requireView().findViewById(R.id.llv_scores)
        scores = SQLiteService.getInstance().getTopTenScores()

        for(i in 0 until llvScores.childCount) {
            var constrScore : ConstraintLayout = llvScores.getChildAt(i) as ConstraintLayout
            var txtPosition : TextView = constrScore.findViewById(R.id.txt_position)
            var txtTime : TextView = constrScore.findViewById(R.id.txt_time)
            var txtGrid : TextView = constrScore.findViewById(R.id.txt_grid)
            var txtDate : TextView = constrScore.findViewById(R.id.txt_date)
            var txtUser : TextView = constrScore.findViewById(R.id.txt_user)

            txtPosition.text = (i + 1).toString()

            if(scores.size <= i) {
                txtTime.text = ""
                txtGrid.text = ""
                txtDate.text = ""
                txtUser.text = ""
            }
            else {
                txtTime.text = scores[i].getTime()
                txtGrid.text = scores[i].getGridSize()
                txtDate.text = scores[i].getDate()
                txtUser.text = scores[i].getUserName()
            }
        }
    }
}