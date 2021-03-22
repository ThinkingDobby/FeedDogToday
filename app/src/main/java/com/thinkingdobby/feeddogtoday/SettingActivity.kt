package com.thinkingdobby.feeddogtoday

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import kotlinx.android.synthetic.main.activity_setting.*
import java.util.*

class SettingActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting)

        // toolBar
        val toolBar: androidx.appcompat.widget.Toolbar? = set_tb
        setSupportActionBar(toolBar)
        supportActionBar!!.setDisplayShowTitleEnabled(false)
        // toolBar

        // Notification AlarmManager
        set_cv_swh_settingNotificationReceiving.setOnClickListener {
            val alarmManager = getSystemService(ALARM_SERVICE) as AlarmManager
            val notFedPetExist = true

            val intent = Intent(this, AlarmReceiver::class.java)
            val pendingIntent = PendingIntent.getBroadcast(
                this, AlarmReceiver.NOTIFICATION_ID, intent,
                PendingIntent.FLAG_UPDATE_CURRENT
            )

            val toastMessage: String
            toastMessage = if (notFedPetExist) {
                val repeatInterval: Long = AlarmManager.INTERVAL_HALF_DAY
                val calendar: Calendar = Calendar.getInstance().apply {
                    timeInMillis = System.currentTimeMillis()
                    set(Calendar.HOUR_OF_DAY, 19)
                    set(Calendar.MINUTE, 0)
                }

                alarmManager.setRepeating(
                    AlarmManager.RTC_WAKEUP,
                    calendar.timeInMillis,
                    repeatInterval,
                    pendingIntent
                )
                "Realtime periodic Alarm On"
            } else {
                alarmManager.cancel(pendingIntent)
                "Realtime periodic Alarm Off"
            }
            Toast.makeText(this, toastMessage, Toast.LENGTH_SHORT).show()
        }

        set_cv_btn_settingNotificationReceiving.setOnClickListener {
            val builder = AlertDialog.Builder(this@SettingActivity)
            builder.setTitle("알림 설정")
            builder.setMessage("\n'시작' 버튼을 누르면 매일 07와 19시에 알림이 전송됩니다.\n" +
                    "\n알림 해제는 디바이스 환경설정에서 가능합니다.")

            builder.setPositiveButton("확인") { _, which ->
            }

            builder.create().show()
        }
        // Notification AlarmManager

        // Init AlarmManager - FOR_DEV
        set_cv_swh_settingDefaultValue.setOnClickListener {
            val initAlarmManager = getSystemService(ALARM_SERVICE) as AlarmManager

            val initIntent = Intent(this, InitReceiver::class.java)
            val initPendingIntent = PendingIntent.getBroadcast(
                this, InitReceiver.NOTIFICATION_ID, initIntent,
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

        set_cv_btn_settingDefaultValue.setOnClickListener {
            val builder = AlertDialog.Builder(this@SettingActivity)
            builder.setTitle("개발자용 초기화 옵션입니다.")

            builder.setPositiveButton("확인") { _, which ->
            }

            builder.create().show()
        }
        // Init AlarmManager - FOR_DEV

        set_btn_back.setOnClickListener {
            finish()
        }
    }

    override fun finish() {
        super.finish()
        overridePendingTransition(R.anim.fadein, R.anim.fadeout)
    }
}
