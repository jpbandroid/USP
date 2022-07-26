package com.jpb.scratchtappy.md3

import android.annotation.TargetApi
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.app.ActivityManager
import android.content.Intent
import android.content.res.Configuration
import android.os.Build
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.cardview.widget.CardView
import android.content.Context
import android.os.Environment
import android.os.StatFs
import com.google.android.material.color.DynamicColors
import com.jaredrummler.android.device.DeviceName
import android.util.DisplayMetrics
import java.text.DecimalFormat
import com.jpb.scratchtappy.md3.utils.DiskUtils

class DeviceInfoActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        DynamicColors.applyIfAvailable(this);
        DeviceName.init(this);
        val ab: androidx.appcompat.app.ActionBar? = supportActionBar
        var orient = "none?!?!?!?"
        ab?.setTitle("About Device")
        ab?.setSubtitle(Build.MODEL)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_device_info)
        val mi = ActivityManager.MemoryInfo()
        val activityManager = getSystemService(ACTIVITY_SERVICE) as ActivityManager
        activityManager.getMemoryInfo(mi)
        val availableMegs: Long = mi.availMem / 0x100000L
        val config: Configuration = this.getResources().getConfiguration()

        val percentAvail: Double = mi.availMem / mi.totalMem.toDouble() * 100.0
        val memtext = findViewById<TextView>(R.id.textView3)
        memtext.text = availableMegs.toString() + " MB"
        val memtext3 = findViewById<TextView>(R.id.textView28)
        memtext3.text = mi.totalMem.toString() + " MB"
        val memtext2 = findViewById<TextView>(R.id.textView27)
        memtext2.text = "(in percentage): " + percentAvail + "%"
        val cputext = findViewById<TextView>(R.id.textView25)
        cputext.text = "CPU arch: " + Build.CPU_ABI
        val cputext2 = findViewById<TextView>(R.id.textView24)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            cputext2.text = Build.SOC_MANUFACTURER
        }
        else {
            cputext2.text = Build.HARDWARE
        }
        if (config.orientation == 1) {
            orient = "Portrait"
        }
        else if (config.orientation == 2) {
            orient = "Landscape"
        }
        else {
            orient = "none?!?!?!?"
        }
        val deviceName = Build.MODEL
        val versionRelease = Build.VERSION.RELEASE
        val manufacturer = Build.MANUFACTURER
        val codename = Build.DEVICE
        val totalInternalValue = "sorry, not available yet"
        val freeInternalValue = "sorry, not available yet"
        //to add to textview
        val textView = findViewById<TextView>(R.id.textView40)
        textView.text = deviceName
        val vextView = findViewById<TextView>(R.id.textView42)
        vextView.text = versionRelease
        val sextView = findViewById<TextView>(R.id.textView38)
        sextView.text = manufacturer
        val securitypatchtext = findViewById<TextView>(R.id.textView48)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            securitypatchtext.text = Build.VERSION.SECURITY_PATCH
        }
        else
        {
            securitypatchtext.text = "To read security patch version, you must have Android 6.0 Marshmallow or over"
        }
        val butdevice = findViewById<View>(R.id.button13) as Button
        butdevice.text = "Device Codename\n" + Build.DEVICE
        butdevice.setOnClickListener {

        }
        val butdevice2 = findViewById<View>(R.id.button14) as Button
        butdevice2.text = "Device Serial Number\n" + Build.SERIAL
        butdevice2.setOnClickListener {

        }
        val butdevice3 = findViewById<View>(R.id.button32) as Button
        butdevice3.text = "Device Board\n" + Build.BOARD
        butdevice3.setOnClickListener {

        }
        var deviceName2 = DeviceName.getDeviceName()
        if (Build.MANUFACTURER == "Amazon")
        {
            if (Build.MODEL == "KFFOWI")
            {
                deviceName2 = "Fire 5th gen (2015)"
            }
            else if (Build.MODEL == "KFAUWI")
            {
                deviceName2 = "Fire 7 7th gen (2017)"
            }
            else if (Build.MODEL == "KFMUWI")
            {
                deviceName2 = "Fire 7 9th gen (2019)"
            }
            else if (Build.MODEL == "KFARWI")
            {
                deviceName2 = "Fire HD 6"
            }
            else if (Build.MODEL == "KFASWI")
            {
                deviceName2 = "Fire HD 7 4th gen (2014)"
            }
            else if (Build.MODEL == "KFMEWI")
            {
                deviceName2 = "Fire HD 8 5th gen (2015)"
            }
            else if (Build.MODEL == "KFTBWI")
            {
                deviceName2 = "Fire HD 10 5th gen (2015)"
            }
            else if (Build.MODEL == "KFGIWI")
            {
                deviceName2 = "Fire HD 8 6th gen (2016)"
            }
            else if (Build.MODEL == "KFDOWI")
            {
                deviceName2 = "Fire HD 8 7th gen (2017)"
            }
            else if (Build.MODEL == "KFSUWI")
            {
                deviceName2 = "Fire HD 10 7th gen (2017)"
            }
            else if (Build.MODEL == "KFKAWI")
            {
                deviceName2 = "Fire HD 8 8th gen (2018)"
            }
            else if (Build.MODEL == "KFMAWI")
            {
                deviceName2 = "Fire HD 10 9th gen (2019)"
            }
            else if (Build.MODEL == "KFONWI")
            {
                deviceName2 = "Fire HD 8/8 Plus 10th gen (2020)"
            }
            else if (Build.MODEL == "KFONWI")
            {
                deviceName2 = "Fire HD 8/8 Plus 10th gen (2020)"
            }
            else if (Build.MODEL == "KFTRWI")
            {
                deviceName2 = "Fire HD 10 11th gen (2021)"
            }
            else if (Build.MODEL == "KFTRPWI")
            {
                deviceName2 = "Fire HD 10 Plus 11th gen (2021)"
            }
            else if (Build.MODEL == "AFTB")
            {
                deviceName2 = "Fire TV (1st gen)"
            }
            else if (Build.MODEL == "AFTM")
            {
                deviceName2 = "Fire TV Stick 1"
            }
            else if (Build.MODEL == "AFTS")
            {
                deviceName2 = "Fire TV 2"
            }
            else if (Build.MODEL == "AFTT")
            {
                deviceName2 = "Fire TV Stick 2"
            }
            else if (Build.MODEL == "AFTN")
            {
                deviceName2 = "Fire TV 3 (pendant form-factor)"
            }
            else if (Build.MODEL == "AFTA")
            {
                deviceName2 = "Fire TV Cube"
            }
            else if (Build.MODEL == "AFTMM")
            {
                deviceName2 = "Fire TV Stick 4K"
            }
            else if (Build.MODEL == "AFTR")
            {
                deviceName2 = "Fire TV Cube 2"
            }
            else if (Build.MODEL == "AFTSSS")
            {
                deviceName2 = "Fire TV Stick 3"
            }
            else if (Build.MODEL == "AFTSS")
            {
                deviceName2 = "Fire TV Stick Lite"
            }
            else if (Build.MODEL == "AFTKA")
            {
                deviceName2 = "Fire TV Stick 4K Max"
            }
            else
            {
                deviceName2 = "Unrecognized Amazon Device!!"
            }
        }

        val butdevice4 = findViewById<View>(R.id.button33) as Button
        butdevice4.text = "User Friendly Device Name\n" + deviceName2
        butdevice4.setOnClickListener {

        }
        val met = DisplayMetrics()
        this.windowManager.defaultDisplay.getMetrics(met) // get display metrics object

        val strSize: String = DecimalFormat("##.##").format(
            Math.sqrt(
                (met.widthPixels / met.xdpi *
                        (met.widthPixels / met.xdpi) +
                        met.heightPixels / met.ydpi * (met.heightPixels / met.ydpi)).toDouble()
            )
        )
        // using Dots per inches with width and height
        val scrntext = findViewById<TextView>(R.id.textView102)
        scrntext.text = strSize + "\""
        val scrntextorient = findViewById<TextView>(R.id.textView104)
        scrntextorient.text = orient
        val scrnsmlwidth = findViewById<TextView>(R.id.textView105)
        scrnsmlwidth.text = "Smallest screen width: " + config.smallestScreenWidthDp + "dp"
        val butandroid = findViewById<View>(R.id.button40) as Button
        butandroid.text = "Fingerprint\n" + Build.FINGERPRINT
        butandroid.setOnClickListener {

        }
        val storagetotaltitle = findViewById<TextView>(R.id.textView51)
        storagetotaltitle.text = DiskUtils.totalSpace(external = false).toString() + " MB"
        val storagefreetitle = findViewById<TextView>(R.id.textView52)
        storagefreetitle.text = "Free storage:\n" + DiskUtils.freeSpace(external = false).toString() + " MB"
    }
    @Deprecated(message = "Deprecated since ST md3 2.0 Enhanced.", replaceWith = ReplaceWith(expression = "com.jpb.scratchtappy.md3.DiskUtils") , level = DeprecationLevel.WARNING)
    fun getAvailableInternalMemorySize(): Long {
        val path = Environment.getDataDirectory()
        val stat = StatFs(path.path)
        val blockSize = stat.blockSizeLong
        val availableBlocks = stat.availableBlocksLong
        return availableBlocks * blockSize
    }
    @Deprecated(message = "Deprecated since ST md3 2.0 Enhanced.", level = DeprecationLevel.WARNING)
    fun getTotalInternalMemorySize(): Long {
        val path = Environment.getDataDirectory()
        val stat = StatFs(path.path)
        val blockSize = stat.blockSizeLong
        val totalBlocks = stat.blockCountLong
        return totalBlocks * blockSize
    }
    @Deprecated(message = "Deprecated since ST md3 2.0 Enhanced.", level = DeprecationLevel.WARNING)
    fun formatSize(size: Long): String {
        if (size <= 0)
            return "0"
        val units = arrayOf("B", "KB", "MB", "GB", "TB")
        val digitGroups = (Math.log10(size.toDouble()) / Math.log10(1024.0)).toInt()
        return DecimalFormat("#,##0.#").format(size / Math.pow(1024.0, digitGroups.toDouble()))
    }
}