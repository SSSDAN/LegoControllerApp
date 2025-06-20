package com.lego.controllerapp.ui

import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SwitchCompat
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
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        sharedPrefs = getSharedPreferences("app_prefs", MODE_PRIVATE)

        modeSwitch = findViewById(R.id.modeSwitch)

        val currentMode = sharedPrefs.getString("mode", "simple")
        modeSwitch.isChecked = currentMode == "advanced"

        modeSwitch.setOnCheckedChangeListener { _, isChecked ->
            val newMode = if (isChecked) "advanced" else "simple"
            sharedPrefs.edit().putString("mode", newMode).apply()
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }
}