package com.lego.controllerapp.ui

import android.content.Context
import android.graphics.Color
import android.util.Log
import android.view.View
import android.widget.FrameLayout
import org.json.JSONException
import org.json.JSONObject
import java.io.File
import java.io.FileNotFoundException

class JsonUiLoader(
    private val context: Context,
    private val container: FrameLayout
) {
    fun loadFromFile(file: File) {
        Log.d("JsonUiLoader", "Попытка загрузить файл: ${file.absolutePath}")

        if (!file.exists()) {
            Log.e("JsonUiLoader", "Файл не найден: $file")
            throw FileNotFoundException("Файл не найден: $file")
        }

        val content = file.readText()
        Log.d("JsonUiLoader", "Содержимое файла: $content")

        if (content.isBlank()) {
            Log.e("JsonUiLoader", "Файл пустой: $file")
            throw JSONException("Файл пустой: $file")
        }

        try {
            val jsonObject = JSONObject(content)
            Log.d("JsonUiLoader", "JSON успешно распарсен")

            val controls = jsonObject.getJSONArray("controls")
            Log.d("JsonUiLoader", "Найдено элементов управления: ${controls.length()}")

            for (i in 0 until controls.length()) {
                val control = controls.getJSONObject(i)
                val type = control.getString("type")
                Log.d("JsonUiLoader", "Обработка элемента $i типа: $type")

                if (type == "stick") {
                    val id = control.getString("id")
                    val position = control.getJSONObject("position")
                    val x = position.getInt("x")
                    val y = position.getInt("y")

                    val size = control.getJSONObject("size")
                    val length = size.getInt("length")
                    val thickness = size.getInt("thickness")

                    Log.d("JsonUiLoader", "Стик $id: x=$x, y=$y, длина=$length, толщина=$thickness")

                    val view = View(context).apply {
                        this.id = View.generateViewId()
                        layoutParams = FrameLayout.LayoutParams(length, thickness).apply {
                            leftMargin = x
                            topMargin = y
                        }
                        setBackgroundColor(Color.BLUE)
                        // отладочная рамка
                        setBackgroundResource(android.R.drawable.alert_dark_frame)
                    }

                    container.addView(view)
                    Log.d("JsonUiLoader", "Добавлен стик с ID=$id")
                }
            }
        } catch (e: Exception) {
            Log.e("JsonUiLoader", "Ошибка при обработке JSON: ${e.message}")
            throw e
        }
    }


}
