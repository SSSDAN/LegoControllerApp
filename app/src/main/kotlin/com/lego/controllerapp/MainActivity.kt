package com.lego.controllerapp

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.lego.controllerapp.ui.SelectModeActivity

class MainActivity : AppCompatActivity() {

    private lateinit var sharedPreferences: android.content.SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        sharedPreferences = getSharedPreferences("lego_controller_app", MODE_PRIVATE)

        // Проверка первого запуска приложения
        if (sharedPreferences.getBoolean("is_first_launch", true)) {
            startActivity(Intent(this, SelectModeActivity::class.java))
            finish() // Закрываем MainActivity, чтобы пользователь не вернулся назад
            return
        }

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        // Здесь можно настроить toolbar, например, заголовок
        supportActionBar?.title = getString(R.string.app_name)
    }

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