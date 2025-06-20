package com.lego.controllerapp.ui

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
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
import com.lego.controllerapp.databinding.ActivityConfigListBinding
import com.lego.controllerapp.viewmodel.ConfigViewModel
import kotlinx.coroutines.launch
import java.io.File

class ConfigListActivity : AppCompatActivity() {

    private lateinit var binding: ActivityConfigListBinding
    private val configViewModel: ConfigViewModel by viewModels()
    private lateinit var adapter: ConfigAdapter

    private var isAdvancedMode: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityConfigListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Настройка тулбара вручную (как в SettingsActivity)
        val toolbar = findViewById<MaterialToolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.title = "Конфигурации"
        supportActionBar?.setDisplayHomeAsUpEnabled(false)

        // Теперь читаем SharedPreferences так же, как в SettingsActivity
        val prefs = getSharedPreferences("app_prefs", MODE_PRIVATE)
        val currentMode = prefs.getString("mode", "simple")
        isAdvancedMode = currentMode == "advanced"

        adapter = ConfigAdapter(
            isAdvanced = isAdvancedMode,
            onEdit = { /* TODO */ },
            onDelete = { configViewModel.delete(it) }
        )

        binding.configRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.configRecyclerView.adapter = adapter

        lifecycleScope.launch {
            configViewModel.configs.collect { configs ->
                adapter.submitList(configs)
            }
        }
    }


    override fun onResume() {
        super.onResume()

        val prefs = getSharedPreferences("app_prefs", MODE_PRIVATE)
        val currentMode = prefs.getString("mode", "simple")
        val newIsAdvanced = currentMode == "advanced"

        // Если режим изменился — перерисовать меню и адаптер
        if (newIsAdvanced != isAdvancedMode) {
            isAdvancedMode = newIsAdvanced
            invalidateOptionsMenu() // перерисует меню
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
                        val result = generateUniqueConfigName(existingNames)
                        name = result?.first ?: return@launch
                        file = result.second
                    } else {
                        file = createEmptyJsonFile(enteredName)
                        if (file == null) return@launch
                        name = enteredName
                    }

                    val config = Config(
                        name = name,
                        filePath = file.absolutePath,
                        type = "custom"
                    )
                    configViewModel.insert(config)
                }
            }
            .setNegativeButton("Отмена", null)
            .show()
    }

    private suspend fun generateUniqueConfigName(existingNames: List<String>): Pair<String, File>? {
        var index = 1
        while (true) {
            val name = "config$index"
            if (name !in existingNames) {
                val file = createEmptyJsonFile(name)
                return file?.let { name to it }
            }
            index++
        }
    }

    private fun createEmptyJsonFile(fileName: String): File? {
        val dir = getExternalFilesDir(null) ?: return null
        val file = File(dir, "$fileName.json")
        if (!file.exists()) {
            file.writeText("{}")
        }
        return file
    }
}
