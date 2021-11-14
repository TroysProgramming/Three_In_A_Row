package com.troysprogramming.three_in_a_row.views

import android.widget.LinearLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.troysprogramming.three_in_a_row.R
import com.troysprogramming.three_in_a_row.models.database.SQLiteService
import com.troysprogramming.three_in_a_row.models.game.GameVMFactory
import com.troysprogramming.three_in_a_row.models.game.HighScore
import com.troysprogramming.three_in_a_row.viewmodels.GameViewModel
import com.troysprogramming.three_in_a_row.viewmodels.HighScoreViewModel

class HighScoreFragment : Fragment(R.layout.layout_highscores) {

    private lateinit var llvScores : LinearLayout
    private lateinit var highScoreVM: HighScoreViewModel

    override fun onStart() {
        super.onStart()

        highScoreVM = ViewModelProvider(this).get(HighScoreViewModel::class.java)

        llvScores = requireView().findViewById(R.id.llv_scores)

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
                highScoreVM.getScores(i).observe(this) { score ->

                    txtTime.text = score.getTime()
                    txtGrid.text = score.getGridSize()
                    txtDate.text = score.getDate()
                    txtUser.text = score.getUserName()
                }
            }
            else
            {
                txtTime.text = ""
                txtGrid.text = ""
                txtDate.text = ""
                txtUser.text = ""
            }
        }
    }
}