package com.lego.controllerapp.data

import androidx.room.TypeConverter

class Converters {
    @TypeConverter
    fun fromConfigType(value: ConfigType): String = value.name

    @TypeConverter
    fun toConfigType(value: String): ConfigType = ConfigType.valueOf(value)
}