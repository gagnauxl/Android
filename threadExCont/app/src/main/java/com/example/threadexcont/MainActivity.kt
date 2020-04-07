package com.example.threadexcont

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.os.SystemClock
import android.util.Log
import android.view.View
import kotlin.concurrent.thread

class MainActivity : AppCompatActivity() {
    private val LOG_TAG = "HEXAGON-LOG::" + object {}.javaClass.enclosingClass?.simpleName
    private val looperThread = ExampleLooperThread()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun startThread(view: View) {
        Log.d(LOG_TAG, "${object {}.javaClass.enclosingMethod?.name.toString()}: starting thread")
        looperThread.start()
    }

    fun stopThread(view: View) {
        Log.d(LOG_TAG, "${object {}.javaClass.enclosingMethod?.name.toString()}: stopping thread")
        looperThread.looper?.quit()
    }

    fun taskA(view: View) {
        // looperThread.handler.post {
        // alternatively
        val threadHandler = Handler(looperThread.looper) // attach handler to the thread

        // potential memory leak, because the anonymous thread class is a inner class which
        // holds a reference to the outer class, the activity
        // So as a long as the thread lives the activity cannot be destroyed
        // better use a static solution

        // with anonymous class
//        var r = object : Runnable {
//            override fun run() {
//                for (i in 1..5) {
//                    Log.d(LOG_TAG, "${object {}.javaClass.enclosingMethod?.name.toString()}: run$i")
//                    SystemClock.sleep(1000)
//                }
//                Log.d(
//                    LOG_TAG,
//                    "${object {}.javaClass.enclosingMethod?.name.toString()}: end of task"
//                )
//            }
//        }
//        threadHandler.post(r)

        // short version
//        threadHandler.post {
//            for (i in 1..5) {
//                Log.d(LOG_TAG, "${object {}.javaClass.enclosingMethod?.name.toString()}: run$i")
//                SystemClock.sleep(1000)
//            }
//            Log.d(LOG_TAG, "${object {}.javaClass.enclosingMethod?.name.toString()}: end of task")
//        }

        var msg: Message = Message.obtain()
        msg.what = ExampleHandler.Companion.TASK_A
        looperThread.handler.sendMessage(msg)

    }

    fun taskB(view: View) {
        var msg: Message = Message.obtain()
        msg.what = ExampleHandler.Companion.TASK_B
        looperThread.handler.sendMessage(msg)
    }
}

