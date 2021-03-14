package com.thinkingdobby.feeddogtoday

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class InitReceiver : BroadcastReceiver() {
    companion object {
        const val TAG = "InitReceiver"
        const val NOTIFICATION_ID = 1
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        Log.d(TAG, "Received intent : $intent")
        val status = false

        val checkUpdates = mutableMapOf<String, Any>()
        checkUpdates.put("breakfastChecked", status)
        checkUpdates.put("dinnerChecked", status)

        val petIdList = mutableListOf<String>()

        FirebaseDatabase.getInstance().getReference("Pets")
            .addValueEventListener(object: ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val pet = snapshot.getValue(Pet::class.java)
                    if (pet != null) {
                        petIdList.add(pet.petId)
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                }
            })

        FirebaseDatabase.getInstance().getReference("Pets").updateChildren(checkUpdates)
    }
}