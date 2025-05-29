package com.lego.controllerapp.ui

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton
import com.lego.controllerapp.MainActivity
import com.lego.controllerapp.R

class SelectModeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_select_mode)

        val easyButton = findViewById<MaterialButton>(R.id.buttonEasy)
        val advancedButton = findViewById<MaterialButton>(R.id.buttonAdvanced)
        val helpButton = findViewById<MaterialButton>(R.id.buttonHelp)

        easyButton.setOnClickListener {
            saveChoice(1)
            startActivity(Intent(this, MainActivity::class.java))
        }

        advancedButton.setOnClickListener {
            saveChoice(2)
            startActivity(Intent(this, EditorActivity::class.java))
        }

        helpButton.setOnClickListener {
            Toast.makeText(this, "Простой — готовое, Продвинутый — с настройкой пульта", Toast.LENGTH_LONG).show()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_select_mode, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.layout.activity_select_mode) {
            saveChoice(2)
            startActivity(Intent(this, EditorActivity::class.java))
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    private fun saveChoice(choice: Int) {
        val sharedPreferences = getSharedPreferences("choice", MODE_PRIVATE)
        sharedPreferences.edit().putInt("choice", choice).apply()
    }
}
