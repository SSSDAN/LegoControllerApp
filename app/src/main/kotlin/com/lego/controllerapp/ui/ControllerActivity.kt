package com.lego.controllerapp.ui

import android.content.pm.ActivityInfo
import android.os.Bundle
import android.util.Log
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import java.io.File
import com.lego.controllerapp.R

class ControllerActivity : AppCompatActivity() {

    private lateinit var jsonElementContainer: FrameLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        setContentView(R.layout.activity_controller)

        jsonElementContainer = findViewById(R.id.jsonElementContainer)

        val filePath = intent.getStringExtra("config_path") ?: return
        val jsonFile = File(filePath)

        if (jsonFile.exists()) {
            JsonUiLoader(this, jsonElementContainer).loadFromFile(jsonFile)
        }

        Log.d("ControllerActivity", "Получен config_path: $filePath")
        Log.d("ControllerActivity", "Ожидаемый путь к JSON: ${jsonFile.absolutePath}")
    }
}
