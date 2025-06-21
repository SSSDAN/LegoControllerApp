package com.lego.controllerapp.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File

@Database(entities = [Config::class], version = 1)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun configDao(): ConfigDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "config_database"
                )
                    .addCallback(object : Callback() {
                        override fun onCreate(db: SupportSQLiteDatabase) {
                            super.onCreate(db)
                            CoroutineScope(Dispatchers.IO).launch {
                                prepopulate(context)
                            }
                        }
                    })
                    .build()
                INSTANCE = instance
                instance
            }
        }

        private suspend fun prepopulate(context: Context) {
            val dao = getDatabase(context).configDao()
            val assetManager = context.assets
            val files = assetManager.list("configs") ?: return
            for (fileName in files) {
                val inputStream = assetManager.open("configs/$fileName")
                val file = File(context.filesDir, fileName)
                inputStream.use { input -> file.outputStream().use { output -> input.copyTo(output) } }

                dao.insert(
                    Config(
                        name = fileName.removeSuffix(".json"),
                        filePath = file.absolutePath,
                        type = ConfigType.SOURCE
                    )
                )
            }
        }
    }
}
