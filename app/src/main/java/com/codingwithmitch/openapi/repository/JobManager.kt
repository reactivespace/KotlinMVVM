package com.codingwithmitch.openapi.repository

import android.util.Log
import kotlinx.coroutines.Job

open class JobManager(
    val className: String
) {
    private val TAG = "AppDebug"

    private val jobs: HashMap<String, Job> = HashMap()


    fun addJob(methodName: String, job: Job) {
        cancelJob(methodName)
        jobs[methodName] = job
    }

    fun cancelJob(methodName: String) {
        getJob(methodName)?.cancel()
    }

    fun getJob(methodName: String): Job? {
        if (jobs.containsKey(methodName)) {
            jobs[methodName]?.let {
                return it
            }
        }
        return null
    }

    fun cancelActiveJobs() {
        for ((methodName, job) in jobs) {
            if (job.isActive) {
                Log.e(TAG, "${className}: cancelling job in method ${methodName}.")
                job.cancel()
            }
        }
    }


}