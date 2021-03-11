package com.thinkingdobby.feeddogtoday

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters

class SimpleWorker(appContext: Context, workerParams: WorkerParameters)
    : Worker(appContext, workerParams) {
    override fun doWork(): Result {

        return Result.success()
    }
}