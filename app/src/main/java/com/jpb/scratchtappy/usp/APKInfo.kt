package com.jpb.scratchtappy.usp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.utils.ColorTemplate
import com.google.android.material.color.DynamicColors


class APKInfo : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        DynamicColors.applyIfAvailable(this);
        setContentView(R.layout.activity_apkinfo2)
        val ab: androidx.appcompat.app.ActionBar? = supportActionBar
        ab?.setTitle("APK Info")
        ab?.setSubtitle("Unified ScratchTappy Platform 9.0")
        val chart = findViewById<PieChart>(R.id.chart)
        val entries: MutableList<PieEntry> = ArrayList()
        entries.add(PieEntry(52.4f, "classes.dex"))
        entries.add(PieEntry(17.1f, "classes2.dex"))
        entries.add(PieEntry(16.5f, "res"))
        entries.add(PieEntry(8.1f, "assets"))
        entries.add(PieEntry(4.1f, "resources.arsc"))
        entries.add(PieEntry(1.5f, "META-INF"))
        entries.add(PieEntry(0.1f, "kotlin"))
        val colors: ArrayList<Int> = ArrayList()
        for (c in ColorTemplate.MATERIAL_COLORS) colors.add(c)
        val set = PieDataSet(entries, "Composition of the USP " + BuildConfig.VERSION_NAME + " app/apk")
        set.setColors(colors);
        val data = PieData(set)
        val legend = chart.legend
        legend.isEnabled = false
        chart.setData(data)
        chart.invalidate() // refresh
    }
}