package com.example.imagesearchapp.SharedPreferences

import android.content.Context
import android.util.Log
import com.example.imagesearchapp.Constans
import com.example.imagesearchapp.RecyclerView.MyLockerListAdapter
import com.example.imagesearchapp.Retrofit.Document
import com.google.gson.Gson
import com.google.gson.JsonParseException
import com.google.gson.reflect.TypeToken

class SharedPreferencesManager(context: Context) {
    private val prefs = context.getSharedPreferences(Constans.PREFS_FILENAME,Context.MODE_PRIVATE)
    private val gson = Gson()
    fun saveSearchInput(data : String) {
        prefs.edit().putString(Constans.KEY_INPUT,data).apply()
    }

    fun loadSearchInput(): String? {
        return prefs.getString(Constans.KEY_INPUT,Constans.DEFAULT_STRING)
    }

    fun saveMyLockerList(list : MutableList<Document>) {
        val json = gson.toJson(list)
        prefs.edit().putString(Constans.KEY_LOCKER,json).apply()
    }

    fun loadMyLockerList() : MutableList<Document>{
        var result : MutableList<Document> = mutableListOf()
        if (prefs.contains(Constans.KEY_LOCKER)) {
            val json = prefs.getString(Constans.KEY_LOCKER, Constans.DEFAULT_STRING)
            try {
                val typeToken = object : TypeToken<MutableList<Document>>() {}.type
                result  = gson.fromJson(json, typeToken)
            } catch (e: JsonParseException) {
                e.printStackTrace()
            }
        }
        return result
    }
}