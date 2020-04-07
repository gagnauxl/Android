package com.example.threadex

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button

class MainActivity : AppCompatActivity() {
    private val LOG_TAG = "HEXAGON-LOG::" + object {}.javaClass.enclosingClass?.simpleName
    private lateinit var buttonStartThread: Button
    private lateinit var buttonStopThread: Button
    @Volatile // Marks the JVM backing field of the annotated property as volatile,
    // meaning that writes to this field are immediately made visible to other threads.
    private var stopThread: Boolean = false

    // alternatively preferred use a local variable which can instantiate the handler and
    // getting the Looper of the main UI thread
    // private var mainUIhandler = Handler()  // Handler associated with the thread it instantiates
    // thus the main UI thread

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        buttonStartThread = findViewById(R.id.button_start_thread)
        buttonStopThread = findViewById(R.id.button_stop_thread)
        buttonStopThread.isEnabled = false
    }

    fun startThread(view: View) {
        stopThread = false
        updateUIState(true)
        // val thread = ExampleThread(10)
        // thread.start()

        // preferred instead of extending
        /*
        val runnable = ExampleRunnable(10)
        Thread(runnable).start()
        */

        // or with anonymous
        Thread {
            for (i in 1..10) {
                if (stopThread) break
                if (i == 5) runOnUiThread { buttonStartThread.text = "50%" }
                Log.d(LOG_TAG, "${object {}.javaClass.enclosingMethod?.name.toString()}: $i")
                try {
                    Thread.sleep(1000)
                } catch (e: InterruptedException) {
                    e.printStackTrace()
                }
            }
            runOnUiThread { updateUIState(false) }
        }.start()
    }

    private fun updateUIState(isThreadStarted: Boolean) {
        buttonStartThread.text = "Start"
        buttonStartThread.isEnabled = !isThreadStarted
        buttonStopThread.isEnabled = isThreadStarted
    }

    fun stopThread(view: View) {
        stopThread = true
        buttonStartThread.text = "Start"
    }

    inner class ExampleThread(var seconds: Int) : Thread() {
        //        private val LOG_TAG = "HEXAGON::" + object {}.javaClass.enclosingClass?.simpleName
        override fun run() {
            for (i in 1..seconds) {
                Log.d(LOG_TAG, "${object {}.javaClass.enclosingMethod?.name.toString()}: $i")
                try {
                    Thread.sleep(1000)
                } catch (e: InterruptedException) {
                    e.printStackTrace()
                }
            }
        }
    }

    inner class ExampleRunnable(var seconds: Int) : Runnable {
        private val LOG_TAG = "HEXAGON::" + object {}.javaClass.enclosingClass?.simpleName
        override fun run() {
            for (i in 1..seconds) {
                if (i == 5) {
                    // buttonStartThread.text = "50%"  // this will trigger a CalledFromWrongThreadException

                    // more convenient this way no need to use a property of the main thread
                    // var mainUIhandler = Handler()
                    // if Handler is instantiated as before --> exexption Can't create handler inside thread that has not called Looper.prepare()
                    // so do it as follows:
                    // 1. Solution
                    /*
                    var mainUIhandler = Handler(Looper.getMainLooper())
                    mainUIhandler.post{
                        buttonStartThread.text = "50%"
                    }
                    */
                    // 2. Solution: does the same as above, internally
                    /*
                    buttonStartThread.post{
                        buttonStartThread.text = "50%"
                    }
                    */

                    // 3. Solution
                    runOnUiThread { buttonStartThread.text = "50%" }

                }
                Log.d(LOG_TAG, "${object {}.javaClass.enclosingMethod?.name.toString()}: $i")
                try {
                    Thread.sleep(1000)
                } catch (e: InterruptedException) {
                    e.printStackTrace()
                }
            }
        }

    }

}
