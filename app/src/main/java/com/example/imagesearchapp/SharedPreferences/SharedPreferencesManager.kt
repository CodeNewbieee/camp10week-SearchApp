package com.example.imagesearchapp.SharedPreferences

import android.content.Context
import com.example.imagesearchapp.Retrofit.Document
import com.google.gson.Gson
import com.google.gson.JsonParseException
import com.google.gson.reflect.TypeToken

class SharedPreferencesManager(context: Context) {
    private val PREFS_FILENAME = "prefs"
    private val DEFAULT_STRING = ""
    private val DEFAULT_INT = 0
    private val DEFAULT_BOOLEAN = false
    private val KEY_INPUT = "inputdata"
    private val KEY_LOCKER = "lockerlist"
    private val prefs = context.getSharedPreferences(PREFS_FILENAME,0)
    private val gson = Gson()

    fun saveSearchInput(data : String) {
        prefs.edit().putString(KEY_INPUT,data).apply()
    }

    fun loadSearchInput(): String? {
        return prefs.getString(KEY_INPUT,DEFAULT_STRING)
    }

    fun saveMyLockerList(list : MutableList<Document>) {
        val json = gson.toJson(list)
        prefs.edit().putString(KEY_LOCKER,json).apply()
    }

    fun loadMyLockerList() {
        if(prefs.contains(KEY_LOCKER)) {
            val json = prefs.getString(KEY_LOCKER,DEFAULT_STRING)
            try {
                val typeToken = object : TypeToken<MutableList<Document>>() {}.type
                return gson.fromJson(json,typeToken)
            }catch (e : JsonParseException) {
                e.printStackTrace()
            }
        }
    }
}