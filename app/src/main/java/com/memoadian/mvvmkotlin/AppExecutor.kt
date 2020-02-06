package com.memoadian.mvvmkotlin

import android.os.Looper
import java.util.concurrent.Executor
import java.util.concurrent.Executors
import android.os.Handler
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
open class AppExecutors (
    val diskIO: Executor,
    val netWorkIO: Executor,
    val mainThread: Executor
) {
    @Inject
    constructor(): this(
        Executors.newSingleThreadExecutor(),//encola las tareas una tras otra
        Executors.newFixedThreadPool(3),//numero de hilos a llamar al web service
        MainThreadExecutor()//ejecutor del hilo principal
    )

    fun diskIO() :Executor {
        return diskIO
    }

    fun netWorkIO(): Executor {
        return netWorkIO
    }

    fun mainThread() : Executor {
        return mainThread
    }

    private class MainThreadExecutor: Executor {
        val mainThreadHandler = Handler(Looper.getMainLooper())
        override fun execute(command: Runnable){
            mainThreadHandler.post(command)
        }
    }
}