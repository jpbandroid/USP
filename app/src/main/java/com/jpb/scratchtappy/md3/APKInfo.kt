package com.jpb.scratchtappy.md3

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
        ab?.setSubtitle("ScratchTappy md3 2.0 Enhanced")
        val chart = findViewById<PieChart>(R.id.chart)
        val entries: MutableList<PieEntry> = ArrayList()
        entries.add(PieEntry(54.5f, "classes.dex"))
        entries.add(PieEntry(14.4f, "res"))
        entries.add(PieEntry(10.1f, "classes11.dex"))
        entries.add(PieEntry(7.1f, "assets"))
        entries.add(PieEntry(5.0f, "lib"))
        entries.add(PieEntry(3.6f, "resources.arsc"))
        entries.add(PieEntry(2.8f, "classes2.dex"))
        entries.add(PieEntry(1.3f, "META-INF"))
        entries.add(PieEntry(0.6f, "classes10.dex"))
        entries.add(PieEntry(0.2f, "classes3.dex"))
        entries.add(PieEntry(0.1f, "kotlin"))
        entries.add(PieEntry(0.1f, "classes4.dex"))
        entries.add(PieEntry(0.1f, "classes9.dex"))
        val colors: ArrayList<Int> = ArrayList()
        for (c in ColorTemplate.MATERIAL_COLORS) colors.add(c)
        val set = PieDataSet(entries, "Composition of the ST md3 " + BuildConfig.VERSION_NAME + " app/apk")
        set.setColors(colors);
        val data = PieData(set)
        val legend = chart.legend
        legend.isEnabled = false
        chart.setData(data)
        chart.invalidate() // refresh
    }
}