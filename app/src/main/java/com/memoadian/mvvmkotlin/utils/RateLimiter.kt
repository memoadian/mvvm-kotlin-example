package com.memoadian.mvvmkotlin.utils

import android.os.SystemClock
import android.util.ArrayMap
import java.util.concurrent.TimeUnit

class RateLimiter <in KEY>(timeout: Int, timeUnit: TimeUnit){
    private val timestamp = ArrayMap<KEY, Long>()
    private val timeout = timeUnit.toMillis(timeout.toLong())

    @Synchronized
    fun shouldFecth(key: KEY):Boolean{
        val lastFeched = timestamp[key]
        val now = now()

        if (lastFeched == null) {
            timestamp[key] = now
            return true
        }

        if (now - lastFeched > timeout) {
            timestamp[key] = now
            return true
        }

        return false
    }

    private fun now () = SystemClock.uptimeMillis()

    @Synchronized
    fun reset (key: KEY) {
        timestamp.remove(key)
    }
}