package com.lego.controllerapp.ui

import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SwitchCompat
import androidx.appcompat.widget.Toolbar
import com.google.android.material.appbar.MaterialToolbar
import com.lego.controllerapp.R

class SettingsActivity : AppCompatActivity() {

    private lateinit var modeSwitch: SwitchCompat
    private lateinit var sharedPrefs: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        val toolbar = findViewById<MaterialToolbar>(R.id.settingsToolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.title = "Настройки"

        sharedPrefs = getSharedPreferences("choice", MODE_PRIVATE)

        modeSwitch = findViewById(R.id.modeSwitch)
        modeSwitch.isChecked = sharedPrefs.getInt("choice", 1) == 2

        modeSwitch.setOnCheckedChangeListener { _, isChecked ->
            sharedPrefs.edit().putInt("choice", if (isChecked) 2 else 1).apply()
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }
}