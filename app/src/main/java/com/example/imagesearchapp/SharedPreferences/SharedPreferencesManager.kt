package com.example.imagesearchapp.SharedPreferences

import android.content.Context

class SharedPreferencesManager(context: Context) {
    private val PREFS_FILENAME = "prefs"
    private val DEFAULT_STRING = ""
    private val DEFAULT_INT = 0
    private val DEFAULT_BOOLEAN = false
    private val prefs = context.getSharedPreferences(PREFS_FILENAME,0)

    fun saveSearchInput(data : String) {
        prefs.edit().putString("input",data).apply()
    }

    fun loadSearchInput(): String? {
        return prefs.getString("input",DEFAULT_STRING)
    }

}