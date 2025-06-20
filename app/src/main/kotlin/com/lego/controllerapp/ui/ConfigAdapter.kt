package com.lego.controllerapp.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.lego.controllerapp.R
import com.lego.controllerapp.data.Config

class ConfigAdapter(
    private val isAdvanced: Boolean,
    private val onEdit: (Config) -> Unit,
    private val onDelete: (Config) -> Unit
) : RecyclerView.Adapter<ConfigAdapter.ConfigViewHolder>() {

    private var configs: List<Config> = emptyList()

    fun submitList(list: List<Config>) {
        configs = if (isAdvanced) list else list.filter { it.type == "source" }
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ConfigViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_config, parent, false)
        return ConfigViewHolder(view)
    }

    override fun onBindViewHolder(holder: ConfigViewHolder, position: Int) {
        val config = configs[position]
        holder.name.text = config.name
        if (isAdvanced && config.type == "custom") {
            holder.edit.visibility = View.VISIBLE
            holder.delete.visibility = View.VISIBLE
            holder.edit.setOnClickListener { onEdit(config) }
            holder.delete.setOnClickListener { onDelete(config) }
        } else {
            holder.edit.visibility = View.GONE
            holder.delete.visibility = View.GONE
        }
    }

    override fun getItemCount() = configs.size

    class ConfigViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val name: TextView = view.findViewById(R.id.configName)
        val edit: ImageButton = view.findViewById(R.id.editButton)
        val delete: ImageButton = view.findViewById(R.id.deleteButton)
    }
}