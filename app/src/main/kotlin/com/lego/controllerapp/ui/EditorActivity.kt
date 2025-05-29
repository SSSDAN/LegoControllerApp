package com.lego.controllerapp.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.lego.controllerapp.R

class EditorActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_editor)

        // Подключаем Toolbar, если он есть в layout
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        if (toolbar != null) {
            setSupportActionBar(toolbar)
            supportActionBar?.setDisplayHomeAsUpEnabled(true) // Кнопка назад
            supportActionBar?.title = "Редактор пульта"
        }

        // TODO: Здесь будет реализация редактора пульта/конфигурации
    }

    // Обработка нажатия на кнопку "назад" в Toolbar
    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }
}