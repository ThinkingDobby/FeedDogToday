package com.thinkingdobby.feeddogtoday

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.google.firebase.database.FirebaseDatabase

class InitReceiver: BroadcastReceiver() {

    companion object {
        const val NOTIFICATION_ID = 1
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        val checkUpdates = mutableMapOf<String, Any>()
        checkUpdates.put("test", true)

        FirebaseDatabase.getInstance().getReference("Pets").child("petId")
            .updateChildren(checkUpdates)
    }
}