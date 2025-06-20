package com.lego.controllerapp.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "configs")
data class Config(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val filePath: String,
    val type: String, // "source" или "custom"
)