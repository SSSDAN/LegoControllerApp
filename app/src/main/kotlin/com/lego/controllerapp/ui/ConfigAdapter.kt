package com.lego.controllerapp.ui

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.lego.controllerapp.R
import com.lego.controllerapp.data.Config
import com.lego.controllerapp.data.ConfigType
import java.io.File

class ConfigAdapter(
    private val isAdvanced: Boolean,
    private val onEdit: (Config) -> Unit,
    private val onDelete: (Config) -> Unit
) : RecyclerView.Adapter<ConfigAdapter.ConfigViewHolder>() {

    private var configs: List<Config> = emptyList()

    fun submitList(list: List<Config>) {
        configs = if (isAdvanced) list else list.filter { it.type == ConfigType.SOURCE }
        notifyDataSetChanged()
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ConfigViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_config, parent, false)
        return ConfigViewHolder(view)
    }

    override fun onBindViewHolder(holder: ConfigViewHolder, position: Int) {
        val config = configs[position]
        holder.name.text = config.name

        holder.edit.visibility = View.GONE
        holder.delete.visibility = View.GONE
        holder.edit.setOnClickListener(null)
        holder.delete.setOnClickListener(null)
        holder.itemView.setOnClickListener {
            val intent = Intent(holder.itemView.context, ControllerActivity::class.java)
            intent.putExtra("config_path", config.filePath)
            holder.itemView.context.startActivity(intent)
        }

        when (config.type) {
            ConfigType.CUSTOM -> {
                if (isAdvanced) {
                    holder.edit.visibility = View.VISIBLE
                    holder.delete.visibility = View.VISIBLE
                    holder.edit.setOnClickListener {
                        onEdit(config)
                    }
                    holder.delete.setOnClickListener {
                        Log.d("DeleteClick", "Нажали удалить: ${config.name}")

                        // Удаление файла
                        val context = holder.itemView.context
                        if (config.type == ConfigType.CUSTOM) {
                            val file = File(config.filePath)
                            Log.d("DeleteClick", "Путь до файла: ${file.absolutePath}")
                            val deleted = file.delete()
                            Log.d("DeleteClick", "Файл удалён: $deleted")
                        }

                        // Удаление записи из БД
                        onDelete(config)
                    }
                }
            }

            ConfigType.SOURCE -> {
                // SOURCE-конфиги не редактируются и не удаляются
                holder.edit.visibility = View.GONE
                holder.delete.visibility = View.GONE
            }
        }
    }


    override fun getItemCount() = configs.size

    class ConfigViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val name: TextView = view.findViewById(R.id.configName)
        val edit: ImageButton = view.findViewById(R.id.editButton)
        val delete: ImageButton = view.findViewById(R.id.deleteButton)
    }
}