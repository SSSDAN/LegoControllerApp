package com.lego.controllerapp.ui

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.lego.controllerapp.R

class SelectModeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_select_mode)

        val easyButton = findViewById<ImageButton>(R.id.buttonEasy)
        val advancedButton = findViewById<ImageButton>(R.id.buttonAdvanced)
        val helpButton = findViewById<ImageButton>(R.id.buttonHelp)

        easyButton.setOnClickListener {
            saveChoice("simple")
            markFirstLaunchComplete()
            startActivity(Intent(this, ConfigListActivity::class.java))
        }

        advancedButton.setOnClickListener {
            saveChoice("advanced")
            markFirstLaunchComplete()
            startActivity(Intent(this, ConfigListActivity::class.java))
        }

        helpButton.setOnClickListener {
            Toast.makeText(
                this,
                "Простой — готовые пульты\nПродвинутый — есть возможность самому создать пульт",
                Toast.LENGTH_LONG
            ).show()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_select_mode, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.layout.activity_select_mode) {
            saveChoice("advanced")
            startActivity(Intent(this, EditorActivity::class.java))
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    private fun saveChoice(mode: String) {
        val sharedPreferences = getSharedPreferences("app_prefs", MODE_PRIVATE)
        sharedPreferences.edit().putString("mode", mode).apply()
    }

    private fun markFirstLaunchComplete() {
        val appPrefs = getSharedPreferences("lego_controller_app", MODE_PRIVATE)
        appPrefs.edit().putBoolean("is_first_launch", false).apply()
    }
}