package com.lavanya.workmanager

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.work.*
import kotlinx.android.synthetic.main.activity_main.*
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val nm = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            nm.createNotificationChannel(
                NotificationChannel(
                    "first", "default",
                    NotificationManager.IMPORTANCE_DEFAULT
                )
            )
        }

        /*For scheduling a one time work request.
         */
        button.setOnClickListener {
            scheduleTask()
        }

        /*For scheduling a periodic time work request.
        */
//        scheduleRepeatingTasks()
    }

    private fun scheduleRepeatingTasks() {

        /*Setting up different constraints on the work request.
         */
        val constraints = Constraints.Builder().apply {
            setRequiredNetworkType(NetworkType.CONNECTED)
            setRequiresCharging(true)
            setRequiresDeviceIdle(false)
            setRequiresStorageNotLow(true)
        }.build()

        val repeatingWork = PeriodicWorkRequestBuilder<NotificationRequestWorker>(
            1,
            TimeUnit.DAYS
        ).setConstraints(constraints)
            .build()
        /*Enqueue the work request to an instance of Work Manager
         */
        WorkManager.getInstance(this).enqueue(repeatingWork)
    }

    private fun scheduleTask() {
        val workerRequest = OneTimeWorkRequestBuilder<NotificationRequestWorker>()
            .setInitialDelay(20, TimeUnit.SECONDS)
            .build()

        WorkManager.getInstance(this).enqueue(workerRequest)
    }
}