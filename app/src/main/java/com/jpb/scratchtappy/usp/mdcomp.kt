package com.jpb.scratchtappy

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.jpb.scratchtappy.MDComponent

class mdcomp : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mdcomp)
        val ab: androidx.appcompat.app.ActionBar? = supportActionBar
        ab?.setTitle("MD Components")
        ab?.setSubtitle("ADL, Holo, MD, MD3")
    }
}