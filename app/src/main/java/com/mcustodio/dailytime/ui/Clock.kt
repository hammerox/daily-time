package com.mcustodio.dailytime.ui

import android.os.Handler
import android.os.SystemClock

class Clock {

    var elapsedTime: Long = 0
    var onTick: ((elapsedTime: Long) -> Unit)? = null
    var isRunning: Boolean = false
        get() = field
        private set

    private val handler = Handler()
    private var time0: Long = 0
    private var time1: Long = 0
    private var deltaTime: Long = 0


    fun setup(initialTime: Long) {
        stop()
        elapsedTime = initialTime
    }

    fun start() {
        time0 = SystemClock.uptimeMillis()
        handler.postDelayed(clockTickCallback, 0)
        isRunning = true
    }

    fun stop() {
        handler.removeCallbacks(clockTickCallback)
        isRunning = false
    }


    private var clockTickCallback = object : Runnable {
        override fun run() {
            time1 = SystemClock.uptimeMillis()
            deltaTime = time1 - time0
            elapsedTime += deltaTime
            time0 = time1

            onTick?.invoke(elapsedTime)

            handler.postDelayed(this, 0)
        }
    }

}