package com.example.jobscheduler

import android.app.job.JobParameters
import android.app.job.JobService
import android.os.SystemClock
import android.util.Log

class ExampleJobService : JobService() {
    private val LOG_TAG = "HEXAGON-LOG::" + object {}.javaClass.enclosingClass?.simpleName
    private var jobCancelled: Boolean = false

    private fun doBackgroundWork(params: JobParameters) {
        Thread(object : Runnable {
            override fun run() {
                for (i in 1..10) {
                    if (jobCancelled) return
                    Log.d(
                        LOG_TAG,
                        "${object {}.javaClass.enclosingMethod?.name.toString()}: run: $i"
                    )
                    SystemClock.sleep(1000)
                }
                Log.d(LOG_TAG, "${object {}.javaClass.enclosingMethod?.name.toString()}: Job finished")
                jobFinished(params, false)
            }
        }).start()


    }

    override fun onStartJob(params: JobParameters?): Boolean {
        Log.d(LOG_TAG, "${object {}.javaClass.enclosingMethod?.name.toString()}: Job started")
        doBackgroundWork(params!!)
        return true
    }

    override fun onStopJob(params: JobParameters?): Boolean {
        Log.d(LOG_TAG, "${object {}.javaClass.enclosingMethod?.name.toString()}: Job cancelled before completion")
        jobCancelled = true
        return true
    }
}