package com.troysprogramming.three_in_a_row.views

import android.content.Context
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import com.troysprogramming.three_in_a_row.R
import yuku.ambilwarna.AmbilWarnaDialog

class SettingsActivity : AppCompatActivity() {

    private lateinit var btnColour1 : ConstraintLayout
    private lateinit var displayColour1 : View

    private lateinit var btnColour2 : ConstraintLayout
    private lateinit var displayColour2 : View

    private lateinit var colorPicker : AmbilWarnaDialog
    private var colour1 : Int = 0
    private var colour2 : Int = 0

    private lateinit var gridSizeList : Array<String>
    private lateinit var dropDownAdapter : ArrayAdapter<String>
    private lateinit var gridSizeText : AutoCompleteTextView

    private val defaultColour1 = Color.RED
    private val defaultColour2 = Color.BLUE
    private lateinit var defaultGridSize : String

    private lateinit var btnReset : Button
    private lateinit var btnSave : Button

    private lateinit var sharedPref : SharedPreferences

    // TODO: For colour picker: https://github.com/yukuku/ambilwarna
    override fun onCreate(sis: Bundle?) {
        super.onCreate(sis)
        setContentView(R.layout.layout_settings)

        sharedPref = this.getSharedPreferences("3inarow_settings.xml", Context.MODE_PRIVATE)

        displayColour1 = findViewById(R.id.view_colour1)
        btnColour1 = findViewById(R.id.constr_colour1)
        btnColour1.setOnClickListener {
            colorPicker = AmbilWarnaDialog(this, colour1,
                object: AmbilWarnaDialog.OnAmbilWarnaListener
                {
                    override fun onOk(dialog: AmbilWarnaDialog?, color: Int) {
                        colour1 = color
                        displayColour1.setBackgroundColor(color)
                    }

                    override fun onCancel(dialog: AmbilWarnaDialog?) {

                    }
            })

            colorPicker.show()
        }

        displayColour2 = findViewById(R.id.view_colour2)
        btnColour2 = findViewById(R.id.constr_colour2)
        btnColour2.setOnClickListener {
            colorPicker = AmbilWarnaDialog(this, colour2,
                object: AmbilWarnaDialog.OnAmbilWarnaListener
                {
                    override fun onOk(dialog: AmbilWarnaDialog?, color: Int) {
                        colour2 = color
                        displayColour2.setBackgroundColor(color)
                    }

                    override fun onCancel(dialog: AmbilWarnaDialog?) {

                    }
            })

            colorPicker.show()
        }

        gridSizeList = resources.getStringArray(R.array.grid_sizes)
        defaultGridSize = gridSizeList[0]
        reloadDropDownAdapter()

        btnReset = findViewById(R.id.btn_reset)
        btnReset.setOnClickListener { resetToDefault() }

        btnSave = findViewById(R.id.btn_save)
        btnSave.setOnClickListener { saveSettings() }

        loadSettings()
    }

    private fun saveSettings() {
        var editor : SharedPreferences.Editor = sharedPref.edit()

        editor.putInt("colour_1", colour1)
        editor.putInt("colour_2", colour2)
        editor.putString("grid_size", gridSizeText.text.toString())

        editor.apply()

        finish()
    }

    private fun loadSettings() {

        colour1 = sharedPref.getInt("colour_1", Color.RED)
        colour2 = sharedPref.getInt("colour_2", Color.BLUE)
        gridSizeText.setText(sharedPref.getString("grid_size", "4 x 4"))
        reloadDropDownAdapter()
        refreshColours()
    }

    private fun resetToDefault() {
        var editor : SharedPreferences.Editor = sharedPref.edit()
        editor.clear()
        editor.apply()

        colour1 = defaultColour1
        colour2 = defaultColour2
        gridSizeText.setText(defaultGridSize)
        reloadDropDownAdapter()
        refreshColours()
    }

    private fun reloadDropDownAdapter() {
        dropDownAdapter = ArrayAdapter(this, R.layout.comp_gridsize_dropdown, gridSizeList)
        gridSizeText = findViewById(R.id.actxt_gridsize)
        gridSizeText.setAdapter(dropDownAdapter)
    }

    private fun refreshColours() {
        displayColour1.setBackgroundColor(colour1)
        displayColour2.setBackgroundColor(colour2)
    }
}