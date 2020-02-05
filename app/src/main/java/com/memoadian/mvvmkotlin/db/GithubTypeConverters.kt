package com.memoadian.mvvmkotlin.db

import android.util.Log
import androidx.room.TypeConverter
import java.lang.NumberFormatException

object GithubTypeConverters {
    @TypeConverter
    @JvmStatic
    fun stringToIntList(data: String?): List<Int>?{
        return data?.let{
            it.split(",").map {
                try {
                    it.toInt()
                } catch (ex: NumberFormatException){
                    Log.d("TAG1", "no se puede convertir a número")
                    null
                }
            }?.filterNotNull()
        }
    }

    @TypeConverter
    @JvmStatic
    fun intListToString(data: List<Int>): String {
        return data?.joinToString { "," }
    }
}