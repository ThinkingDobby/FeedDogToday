package com.thinkingdobby.feeddogtoday

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class ListActivity : AppCompatActivity() {

    val petList = mutableListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list)
    }
}
