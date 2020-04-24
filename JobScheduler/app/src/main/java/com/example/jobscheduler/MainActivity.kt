package com.example.jobscheduler

import android.app.job.JobInfo
import android.app.job.JobScheduler
import android.content.ComponentName
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.content.getSystemService

class MainActivity : AppCompatActivity() {
    private val LOG_TAG = "HEXAGON-LOG::" + object {}.javaClass.enclosingClass?.simpleName

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun scheduleJob(view: View) {
        val componentName = ComponentName(this, ExampleJobService::class.java)
        val jobInfo: JobInfo = JobInfo.Builder(123, componentName).apply {
//            setRequiresCharging(true)
            setRequiredNetworkType(JobInfo.NETWORK_TYPE_UNMETERED)
            setPeriodic(15 * 60 * 1000)
        }.build()
        val scheduler = getSystemService(Context.JOB_SCHEDULER_SERVICE) as JobScheduler
        var resultCode = scheduler.schedule(jobInfo)
        if (resultCode == JobScheduler.RESULT_SUCCESS) {
            Log.d(LOG_TAG, "${object {}.javaClass.enclosingMethod?.name.toString()}: Job scheduled")
        } else {
            Log.d(
                LOG_TAG,
                "${object {}.javaClass.enclosingMethod?.name.toString()}: Job scheduling failed"
            )
        }
    }

    fun cancelJob(view: View) {
        val scheduler = getSystemService(Context.JOB_SCHEDULER_SERVICE) as JobScheduler
        scheduler.cancel(123)
        Log.d(LOG_TAG, "${object {}.javaClass.enclosingMethod?.name.toString()}: Job cancelled")
    }
}
