package com.lego.controllerapp.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface ConfigDao {
    @Query("SELECT * FROM configs")
    fun getAllConfigsFlow(): Flow<List<Config>>

    @Query("SELECT * FROM configs")
    suspend fun getAllConfigsSuspend(): List<Config>

    @Query("SELECT name FROM configs")
    suspend fun getAllNames(): List<String>

    @Insert
    suspend fun insert(config: Config)

    @Delete
    suspend fun delete(config: Config)
}
