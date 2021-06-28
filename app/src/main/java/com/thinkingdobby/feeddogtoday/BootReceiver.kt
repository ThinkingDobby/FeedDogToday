package com.thinkingdobby.feeddogtoday

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import java.util.*

class BootReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent?.action == "android.intent.action.BOOT_COMPLETED") {
            val initAlarmManager = context?.getSystemService(Context.ALARM_SERVICE) as AlarmManager

            val initIntent = Intent(context, InitReceiver::class.java)
            val initPendingIntent = PendingIntent.getBroadcast(
                context, InitReceiver.NOTIFICATION_ID, initIntent,
                PendingIntent.FLAG_UPDATE_CURRENT
            )

            val initRepeatInterval: Long = AlarmManager.INTERVAL_DAY
            val initCalendar: Calendar = Calendar.getInstance().apply {
                timeInMillis = System.currentTimeMillis()
                set(Calendar.HOUR_OF_DAY, 0)
                set(Calendar.MINUTE, 0)
            }

            initAlarmManager.setRepeating(
                AlarmManager.RTC_WAKEUP,
                initCalendar.timeInMillis,
                initRepeatInterval,
                initPendingIntent
            )
        }
    }
}