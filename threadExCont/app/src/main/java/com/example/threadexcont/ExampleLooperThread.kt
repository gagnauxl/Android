package com.example.threadexcont

import android.os.Handler
import android.os.Looper
import android.os.SystemClock
import android.util.Log

class ExampleLooperThread: Thread() {
    private val LOG_TAG = "HEXAGON-LOG::" + object {}.javaClass.enclosingClass?.simpleName
    lateinit var handler: Handler
    var looper: Looper? = null

    override fun run() {
        Looper.prepare()
        looper = Looper.myLooper()
        handler = ExampleHandler()
        Looper.loop()        // endless loop, take care not to create a memory leak
        Log.d(LOG_TAG, "${object {}.javaClass.enclosingMethod?.name.toString()}: end of run")
    }
}