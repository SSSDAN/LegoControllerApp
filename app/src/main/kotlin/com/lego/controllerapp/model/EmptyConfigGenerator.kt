package com.lego.controllerapp.model

import android.content.Context
import java.io.File

object EmptyConfigGenerator {
    fun createEmptyConfig(context: Context, name: String): File {
        val file = File(context.getExternalFilesDir(null), "$name.json")
        file.writeText("{\"elements\":[]}") // минимальный JSON
        return file
    }
}