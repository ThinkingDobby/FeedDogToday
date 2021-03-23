package com.thinkingdobby.feeddogtoday

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class InitReceiver: BroadcastReceiver() {

    companion object {
        const val TAG = "AlarmReceiver"
        const val NOTIFICATION_ID = 1
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        Log.d(TAG, "Received intent : $intent")

        val checkInit = mutableMapOf<String, Any>()
        val status = false
        checkInit.put("breakfastChecked", status)
        checkInit.put("dinnerChecked", status)

        var initDone = false

        FirebaseDatabase.getInstance().getReference("Pets")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (!initDone) {
                        for (postSnapShot in snapshot.children) {
                            FirebaseDatabase.getInstance().getReference("Pets")
                                .child(postSnapShot.key.toString()).updateChildren(checkInit)
                        }
                        initDone = true
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                }
            })
    }
}
