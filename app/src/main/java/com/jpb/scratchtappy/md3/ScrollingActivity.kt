package com.jpb.scratchtappy.md3

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import com.google.android.material.appbar.CollapsingToolbarLayout
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import com.jpb.scratchtappy.md3.databinding.ActivityScrollingBinding
import androidx.browser.customtabs.CustomTabsIntent;
import androidx.core.content.ContextCompat;
import android.content.Intent

import android.app.Activity
import android.net.Uri
import com.afollestad.materialdialogs.MaterialDialog
import com.google.android.gms.oss.licenses.OssLicensesMenuActivity
import com.google.android.material.color.DynamicColors
import com.jpb.scratchtappy.md3.R





class ScrollingActivity : AppCompatActivity() {
    // url for loading in custom chrome tab
    var url = "https://occoam.com/jpb/"
    var url2 = "https://www.youtube.com/channel/UCojnty8ChkeDPEXHa56kOoA"

    private lateinit var binding: ActivityScrollingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        DynamicColors.applyIfAvailable(this);

        binding = ActivityScrollingBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val butdevice = binding.root.findViewById<View>(com.jpb.scratchtappy.md3.R.id.button12) as Button
        butdevice.setOnClickListener {
            val intent = Intent(applicationContext, DeviceInfoActivity::class.java)
            startActivity(intent)
        }
        val butmd2 = binding.root.findViewById<View>(com.jpb.scratchtappy.md3.R.id.button35) as Button
        butmd2.setOnClickListener {
            val intent = Intent(applicationContext, mdcomp::class.java)
            startActivity(intent)
        }
        val butalllicence = binding.root.findViewById<View>(com.jpb.scratchtappy.md3.R.id.button39) as Button
        butalllicence.setOnClickListener {
            val intent = Intent(applicationContext, OssLicensesMenuActivity::class.java)
            startActivity(intent)
        }
        val butjpb = binding.root.findViewById<View>(com.jpb.scratchtappy.md3.R.id.button4) as Button
        butjpb.setOnClickListener {
            // initializing object for custom chrome tabs.
            // initializing object for custom chrome tabs.
            val customIntent = CustomTabsIntent.Builder()

            // below line is setting toolbar color
            // for our custom chrome tab.

            // below line is setting toolbar color
            // for our custom chrome tab.
            customIntent.setToolbarColor(
                ContextCompat.getColor(
                    this@ScrollingActivity,
                    R.color.jpb
                )
            )

            // we are calling below method after
            // setting our toolbar color.

            // we are calling below method after
            // setting our toolbar color.
            openCustomTab(this@ScrollingActivity, customIntent.build(), Uri.parse(url))
        }
        val butjpbyt = binding.root.findViewById<View>(com.jpb.scratchtappy.md3.R.id.button3) as Button
        butjpbyt.setOnClickListener {
            // initializing object for custom chrome tabs.
            // initializing object for custom chrome tabs.
            val customIntent = CustomTabsIntent.Builder()

            // below line is setting toolbar color
            // for our custom chrome tab.

            // below line is setting toolbar color
            // for our custom chrome tab.
            customIntent.setToolbarColor(
                ContextCompat.getColor(
                    this@ScrollingActivity,
                    R.color.yt
                )
            )

            // we are calling below method after
            // setting our toolbar color.

            // we are calling below method after
            // setting our toolbar color.
            openCustomTab(this@ScrollingActivity, customIntent.build(), Uri.parse(url2))
        }
        val butandroid = binding.root.findViewById<View>(com.jpb.scratchtappy.md3.R.id.button5) as Button
        butandroid.setOnClickListener {
            MaterialDialog(this).show {
                title(R.string.android)
                message(R.string.licenceandroid)
                icon(R.drawable.ic_baseline_android_24)
            }
        }
        val butmd = binding.root.findViewById<View>(com.jpb.scratchtappy.md3.R.id.button6) as Button
        butmd.setOnClickListener {
            MaterialDialog(this).show {
                title(R.string.md)
                message(R.string.licenceandroid)
                icon(R.drawable.ic_material_design)
            }
        }
        val butabout = binding.root.findViewById<View>(com.jpb.scratchtappy.md3.R.id.button8) as Button
        butabout.setOnClickListener {
            MaterialDialog(this).show {
                title(R.string.about)
                message(R.string.licencedrakeet)
                icon(R.drawable.ic_baseline_info_24)
            }
        }
        val butmultitype = binding.root.findViewById<View>(com.jpb.scratchtappy.md3.R.id.button9) as Button
        butmultitype.setOnClickListener {
            MaterialDialog(this).show {
                title(R.string.multitype)
                message(R.string.licencedrakeet)
                icon(R.drawable.ic_baseline_info_24)
            }
        }
        val butmddialogs = binding.root.findViewById<View>(com.jpb.scratchtappy.md3.R.id.button10) as Button
        butmddialogs.setOnClickListener {
            MaterialDialog(this).show {
                title(R.string.afollestaddialog)
                message(R.string.licenceafollestad)
                icon(R.drawable.ic_baseline_info_24)
            }
        }
        val butglide = binding.root.findViewById<View>(com.jpb.scratchtappy.md3.R.id.button11) as Button
        butglide.setOnClickListener {
            MaterialDialog(this).show {
                title(R.string.glide)
                message(R.string.licenceglide)
                icon(R.drawable.ic_baseline_info_24)
            }
        }
        val butapk = binding.root.findViewById<View>(com.jpb.scratchtappy.md3.R.id.button2) as Button
        butapk.setOnClickListener {
            val intent = Intent(applicationContext, APKInfo::class.java)
            startActivity(intent)
        }
    }
    fun openCustomTab(activity: Activity, customTabsIntent: CustomTabsIntent, uri: Uri?) {
        // package name is the default package
        // for our custom chrome tab
        val packageName = "com.android.chrome"
        if (packageName != null) {

            // we are checking if the package name is not null
            // if package name is not null then we are calling
            // that custom chrome tab with intent by passing its
            // package name.
            customTabsIntent.intent.setPackage(packageName)

            // in that custom tab intent we are passing
            // our url which we have to browse.
            customTabsIntent.launchUrl(activity, uri!!)
        } else {
            // if the custom tabs fails to load then we are simply
            // redirecting our user to users device default browser.
            activity.startActivity(Intent(Intent.ACTION_VIEW, uri))
        }
    }
}