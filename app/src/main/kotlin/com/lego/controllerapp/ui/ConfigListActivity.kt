package com.lego.controllerapp.ui

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.EditText
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.appbar.MaterialToolbar
import com.lego.controllerapp.R
import com.lego.controllerapp.data.Config
import com.lego.controllerapp.data.ConfigType
import com.lego.controllerapp.databinding.ActivityConfigListBinding
import com.lego.controllerapp.viewmodel.ConfigViewModel
import kotlinx.coroutines.launch
import java.io.File

class ConfigListActivity : AppCompatActivity() {

    private lateinit var binding: ActivityConfigListBinding
    private val configViewModel: ConfigViewModel by viewModels()
    private lateinit var adapter: ConfigAdapter
    private lateinit var prefsHelper: SharedPreferencesHelper
    private var isAdvancedMode: Boolean = false


    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding = ActivityConfigListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        copyAssetsToInternalStorageForce()

        prefsHelper = SharedPreferencesHelper(this)
        val isAdvanced = prefsHelper.isAdvancedMode()

        val toolbar = findViewById<MaterialToolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.title = "Конфигурации"
        supportActionBar?.setDisplayHomeAsUpEnabled(false)

        adapter = ConfigAdapter(
            isAdvanced = isAdvancedMode,
            onEdit = { /* TODO */ },
            onDelete = { config ->
                lifecycleScope.launch {
                    configViewModel.delete(config)
                }
            }
        )


        binding.configRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.configRecyclerView.adapter = adapter

        lifecycleScope.launch {
            configViewModel.configs.collect { configs ->
                val isAdvanced = SharedPreferencesHelper(this@ConfigListActivity).isAdvancedMode()
                val filtered = if (isAdvanced) {
                    configs
                } else {
                    configs.filter { it.type == ConfigType.SOURCE }
                }
                adapter.submitList(filtered)
            }
        }
    }


    override fun onResume() {
        super.onResume()

        val prefs = getSharedPreferences("app_prefs", MODE_PRIVATE)
        val currentMode = prefs.getString("mode", "simple")
        val newIsAdvanced = currentMode == "advanced"

        if (newIsAdvanced != isAdvancedMode) {
            isAdvancedMode = newIsAdvanced
            invalidateOptionsMenu()
            adapter = ConfigAdapter(
                isAdvanced = isAdvancedMode,
                onEdit = { /* TODO */ },
                onDelete = { configViewModel.delete(it) }
            )
            binding.configRecyclerView.adapter = adapter
            lifecycleScope.launch {
                configViewModel.configs.collect { adapter.submitList(it) }
            }
        }
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_config_list, menu)
        return true
    }

    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        val addItem = menu?.findItem(R.id.action_add_config)
        addItem?.isVisible = isAdvancedMode
        return super.onPrepareOptionsMenu(menu)
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_add_config -> {
                showAddConfigDialog()
                true
            }

            R.id.action_settings -> {
                startActivity(Intent(this, SettingsActivity::class.java))
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun showAddConfigDialog() {
        val input = EditText(this)
        input.hint = "Введите имя конфигурации"
        AlertDialog.Builder(this)
            .setTitle("Новая конфигурация")
            .setView(input)
            .setPositiveButton("Создать") { _, _ ->
                val enteredName = input.text.toString().trim()
                lifecycleScope.launch {
                    val existingNames = configViewModel.getAllConfigsSuspend().map { it.name }
                    val name: String
                    val file: File?

                    if (enteredName.isEmpty() || enteredName in existingNames) {
                        val result = generateUniqueConfigName(this@ConfigListActivity, existingNames)
                        name = result?.first ?: return@launch
                        file = result.second
                    } else {
                        file = createEmptyJsonFile(this@ConfigListActivity, enteredName)
                        if (file == null) return@launch
                        name = enteredName
                    }

                    val config = Config(
                        name = name,
                        filePath = file.absolutePath,
                        type = ConfigType.CUSTOM
                    )
                    configViewModel.insert(config)
                }
            }
            .setNegativeButton("Отмена", null)
            .show()
    }

    private suspend fun generateUniqueConfigName(
        context: Context,
        existingNames: List<String>
    ): Pair<String, File>? {
        var index = 1
        while (true) {
            val name = "config$index"
            if (name !in existingNames) {
                val file = createEmptyJsonFile(context, name)
                return name to file
            }
            index++
        }
    }


    private fun copyAssetsToInternalStorageForce() {
        val files = listOf(
            "Обычное управление.json",
            "Танковое управление.json"
        )

        files.forEach { fileName ->
            val targetFile = File(filesDir, fileName)

            try {
                val input = assets.open(fileName)
                val content = input.bufferedReader().use { it.readText() }

                Log.d("AssetCopy", "Содержимое $fileName: $content")

                targetFile.writeText(content)
                Log.d("AssetCopy", "Перезаписали файл: $fileName")
            } catch (e: Exception) {
                Log.e("AssetCopy", "Ошибка при копировании $fileName: ${e.message}")
            }
        }
    }



    fun createEmptyJsonFile(context: Context, name: String): File {
        val file = File(context.filesDir, "$name.json")
        file.writeText("{}")
        Log.d("FileCreate", "Создан файл: ${file.absolutePath}")
        return file
    }
}
