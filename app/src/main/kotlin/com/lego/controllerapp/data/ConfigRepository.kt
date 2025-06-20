package com.lego.controllerapp.data

import android.content.Context
import kotlinx.coroutines.flow.Flow

class ConfigRepository(context: Context) {

    private val dao: ConfigDao = AppDatabase.getDatabase(context).configDao()

    fun getAllConfigsFlow(): Flow<List<Config>> = dao.getAllConfigsFlow()

    suspend fun getAllConfigsSuspend(): List<Config> = dao.getAllConfigsSuspend()

    suspend fun insert(config: Config) = dao.insert(config)

    suspend fun delete(config: Config) = dao.delete(config)
}
