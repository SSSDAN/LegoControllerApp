package com.lego.controllerapp.ui

import android.content.Context
import androidx.preference.PreferenceManager

class SharedPreferencesHelper(private val context: Context) {
    private val preference = PreferenceManager.getDefaultSharedPreferences(context)

    fun getPreference(key: String): String? {
        return preference.getString(key, "")
    }

    fun setPreference(key: String, value: String) {
        preference.edit().putString(key, value).apply()
    }

    fun isAdvancedMode(): Boolean {
        return getPreference("mode") == "advanced"
    }

}
