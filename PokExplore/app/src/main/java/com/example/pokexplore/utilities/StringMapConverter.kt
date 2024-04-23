package com.example.pokexplore.utilities

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

object StringMapConverter {
    @TypeConverter
    fun jsonToMap(value: String): Map<String, Int>? {
        val mapType = object : TypeToken<Map<String, Int>?>() {}.type
        return Gson().fromJson(value, mapType)
    }

    @TypeConverter
    fun mapToJson(map: Map<String, Int>?): String {
        val gson = Gson()
        return gson.toJson(map)
    }
}