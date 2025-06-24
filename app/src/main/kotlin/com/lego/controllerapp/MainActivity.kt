package com.lego.controllerapp

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.lego.controllerapp.ui.SelectModeActivity
import com.lego.controllerapp.ui.ConfigListActivity
import com.lego.controllerapp.ui.SharedPreferencesHelper

class MainActivity : AppCompatActivity() {

    private lateinit var sharedPreferences: android.content.SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val prefsHelper = SharedPreferencesHelper(this)

        if (prefsHelper.getPreference("is_first_launch") != "false") {
            startActivity(Intent(this, SelectModeActivity::class.java))
            prefsHelper.setPreference("is_first_launch", "false")
            finish()
            return
        }

        startActivity(Intent(this, ConfigListActivity::class.java))
        finish()


        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        // Здесь можно настроить toolbar, например, заголовок
        supportActionBar?.title = getString(R.string.app_name)
    }

    @SuppressLint("ResourceType")
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.layout.activity_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.layout.activity_select_mode -> {
                // Пока показываем тост как пример
                Toast.makeText(this, "Пункт меню выбран", Toast.LENGTH_SHORT).show()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}