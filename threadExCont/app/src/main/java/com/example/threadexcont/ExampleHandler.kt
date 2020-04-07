package com.example.threadexcont

import android.os.Handler
import android.os.Message
import android.util.Log

class ExampleHandler : Handler(){
    private val LOG_TAG = "HEXAGON-LOG::" + object {}.javaClass.enclosingClass?.simpleName
    companion object {
        val TASK_A: Int = 1
        val TASK_B: Int = 2
    }

    override fun handleMessage(msg: Message) {
        when(msg.what) {
            TASK_A -> Log.d(
                LOG_TAG,
                "${object {}.javaClass.enclosingMethod?.name.toString()}: Task A executed"
            )
            TASK_B -> Log.d(
                LOG_TAG,
                "${object {}.javaClass.enclosingMethod?.name.toString()}: Task B executed"
            )
        }
    }


}