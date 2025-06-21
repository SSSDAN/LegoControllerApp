package com.lego.controllerapp.data

import android.R.attr.name
import android.content.Context
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.File

@Entity(tableName = "configs")
data class Config(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val filePath: String,
    val type: ConfigType
){
    // Функция для получения пути к файлу
    fun getFile(context: Context): File {
        return File(context.filesDir, "$name.json")
    }
}