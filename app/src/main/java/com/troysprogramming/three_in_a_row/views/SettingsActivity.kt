package com.troysprogramming.three_in_a_row.views

import android.content.Context
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.ViewModelProvider
import com.troysprogramming.three_in_a_row.R
import com.troysprogramming.three_in_a_row.viewmodels.SettingsViewModel
import com.troysprogramming.three_in_a_row.viewmodels.vmfactory.SettingsVMFactory
import yuku.ambilwarna.AmbilWarnaDialog

class SettingsActivity : AppCompatActivity(), OnItemSelectedListener {

    private lateinit var btnColour1 : ConstraintLayout
    private lateinit var displayColour1 : View

    private lateinit var btnColour2 : ConstraintLayout
    private lateinit var displayColour2 : View

    private lateinit var colorPicker : AmbilWarnaDialog

    private lateinit var dropDownAdapter : ArrayAdapter<String>
    private lateinit var gridSizeText : Spinner

    private lateinit var btnReset : Button
    private lateinit var btnSave : Button

    private lateinit var sharedPref : SharedPreferences
    private lateinit var editor: SharedPreferences.Editor

    private lateinit var settingsVM: SettingsViewModel

    private var col: Int = 0

    override fun onCreate(sis: Bundle?) {

        sharedPref = this.getSharedPreferences("3inarow_settings.xml", Context.MODE_PRIVATE)
        editor = sharedPref.edit()

        super.onCreate(sis)
        setContentView(R.layout.layout_settings)

        initVM()

        displayColour1 = findViewById(R.id.view_colour1)
        btnColour1 = findViewById(R.id.constr_colour1)
        btnColour1.setOnClickListener {
            col = settingsVM.getColour1().value!!
            colorPicker = AmbilWarnaDialog(this, col,
                object: AmbilWarnaDialog.OnAmbilWarnaListener
                {
                    override fun onOk(dialog: AmbilWarnaDialog?, color: Int) {
                        col = color
                        settingsVM.applyColour1(col)
                    }

                    override fun onCancel(dialog: AmbilWarnaDialog?) { }
                })

            colorPicker.show()
        }

        displayColour2 = findViewById(R.id.view_colour2)
        btnColour2 = findViewById(R.id.constr_colour2)
        btnColour2.setOnClickListener {
            col = settingsVM.getColour2().value!!
            colorPicker = AmbilWarnaDialog(this, col,
                object: AmbilWarnaDialog.OnAmbilWarnaListener
                {
                    override fun onOk(dialog: AmbilWarnaDialog?, color: Int) {
                        col = color
                        settingsVM.applyColour2(col)
                    }

                    override fun onCancel(dialog: AmbilWarnaDialog?) { }
            })

            colorPicker.show()
        }

        btnReset = findViewById(R.id.btn_reset)
        btnReset.setOnClickListener { resetToDefault() }

        btnSave = findViewById(R.id.btn_save)
        btnSave.setOnClickListener { saveSettings() }

        reloadDropDownAdapter()
        observeSettings()
    }

    private fun saveSettings() { finish() }

    private fun resetToDefault() {
        editor.clear()
        editor.apply()
        viewModelStore.clear()
        recreate()
    }

    private fun reloadDropDownAdapter() {
        dropDownAdapter = ArrayAdapter(
            this,
            R.layout.comp_gridsize_dropdown,
            settingsVM.getGridSizeList()
        )
        gridSizeText = findViewById(R.id.spn_gridsize)
        gridSizeText.adapter = dropDownAdapter
        gridSizeText.setSelection(settingsVM.getGridSizeList()
            .indexOf(settingsVM.getGridSize().value))
        gridSizeText.onItemSelectedListener = this
    }

    private fun initVM() {
        settingsVM = ViewModelProvider(this, SettingsVMFactory(
            sharedPref.getInt("colour_1", Color.RED),
            sharedPref.getInt("colour_2", Color.BLUE),
            sharedPref.getString("grid_size", "4 x 4")!!,
            resources.getStringArray(R.array.grid_sizes)
        )).get(SettingsViewModel::class.java)
    }

    private fun observeSettings() {
        settingsVM.getColour1().observe(this, { col1 ->
            displayColour1.setBackgroundColor(col1)
            editor.putInt("colour_1", col1).apply()
        })
        settingsVM.getColour2().observe(this, { col2 ->
            displayColour2.setBackgroundColor(col2)
            editor.putInt("colour_2", col2).apply()
        })
        settingsVM.getGridSize().observe(this, { gridSize ->
            editor.putString("grid_size", gridSize).apply()
        })
    }

    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
        settingsVM.onGridSizeSelected(p0!!.getItemAtPosition(p2) as String)
    }

    override fun onNothingSelected(p0: AdapterView<*>?) { }
}