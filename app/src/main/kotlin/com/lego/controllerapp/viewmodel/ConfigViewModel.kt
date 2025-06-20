package com.lego.controllerapp.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.lego.controllerapp.data.Config
import com.lego.controllerapp.data.ConfigRepository
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.launch

class ConfigViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = ConfigRepository(application)

    // Поток для списка конфигураций
    val configs: StateFlow<List<Config>> = repository.getAllConfigsFlow()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Удаление
    fun delete(config: Config) = viewModelScope.launch {
        repository.delete(config)
    }

    // Добавление
    fun insert(config: Config) = viewModelScope.launch {
        repository.insert(config)
    }

    // Suspend-функция для получения списка конфигураций
    suspend fun getAllConfigsSuspend(): List<Config> {
        return repository.getAllConfigsSuspend()
    }
}