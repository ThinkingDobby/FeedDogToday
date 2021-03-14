package com.thinkingdobby.feeddogtoday

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.google.firebase.database.FirebaseDatabase

class InitReceiver: BroadcastReceiver() {

    companion object {
        const val TAG = "AlarmReceiver"
        const val NOTIFICATION_ID = 1
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        Log.d(TAG, "Received intent : $intent")

        val checkUpdates = mutableMapOf<String, Any>()
        val status = false
        checkUpdates.put("breakfastChecked", status)
        checkUpdates.put("dinnerChecked", status)

        val ref = FirebaseDatabase.getInstance().getReference("Pets").child("petId")

        ref.updateChildren(checkUpdates)
    }
}
