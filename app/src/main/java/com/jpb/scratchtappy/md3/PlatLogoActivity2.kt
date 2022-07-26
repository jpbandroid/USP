/*
 * Copyright (C) 2010 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.jpb.scratchtappy.md3

import com.jpb.scratchtappy.md3.getLong
import com.jpb.scratchtappy.md3.putLong
import android.app.Activity
import com.jpb.scratchtappy.md3.DessertCaseView
import android.content.pm.PackageManager
import android.content.ComponentName
import com.jpb.scratchtappy.md3.DessertCaseDream
import com.jpb.scratchtappy.md3.DessertCaseView.RescalingContainer
import kotlin.jvm.JvmOverloads
import android.widget.FrameLayout
import android.util.SparseArray
import android.graphics.drawable.Drawable
import kotlin.jvm.Synchronized
import android.animation.AnimatorListenerAdapter
import android.view.animation.AccelerateInterpolator
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.view.animation.AnticipateOvershootInterpolator
import com.jpb.scratchtappy.md3.R
import android.graphics.Bitmap
import android.graphics.ColorMatrixColorFilter
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.service.dreams.DreamService
import android.os.Bundle
import android.util.DisplayMetrics
import android.graphics.Typeface
import android.view.Gravity
import com.jpb.scratchtappy.md3.PlatLogoActivity2
import android.widget.TextView
import android.view.View.OnLongClickListener
import android.content.Intent
import com.jpb.scratchtappy.md3.DessertCase
import android.content.ActivityNotFoundException
import android.os.Handler
import android.text.method.TransformationMethod
import android.util.Log
import android.view.View
import android.view.animation.DecelerateInterpolator
import android.widget.ImageView
import com.jpb.scratchtappy.md3.TransformationMethod2

class PlatLogoActivity2 : Activity() {
    var mContent: FrameLayout? = null
    var mCount = 0
    val mHandler = Handler()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val metrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(metrics)
        val bold = Typeface.create("sans-serif", Typeface.BOLD)
        val light = Typeface.create("sans-serif-light", Typeface.NORMAL)
        mContent = FrameLayout(this)
        mContent!!.setBackgroundColor(-0x40000000)
        val lp = FrameLayout.LayoutParams(
            FrameLayout.LayoutParams.WRAP_CONTENT,
            FrameLayout.LayoutParams.WRAP_CONTENT
        )
        lp.gravity = Gravity.CENTER
        val logo = ImageView(this)
        logo.setImageResource(R.drawable.jpb)
        logo.scaleType = ImageView.ScaleType.CENTER_INSIDE
        logo.visibility = View.INVISIBLE
        val bg = View(this)
        bg.setBackgroundColor(BGCOLOR)
        bg.alpha = 0f
        val letter = TextView(this)
        letter.typeface = bold
        letter.textSize = 300f
        letter.setTextColor(-0x1)
        letter.gravity = Gravity.CENTER
        //        letter.setText(String.valueOf(Build.ID).substring(0, 1));
        letter.text = "J"
        val p = (4 * metrics.density).toInt()
        val tv = TextView(this)
        if (light != null) tv.typeface = light
        tv.textSize = 30f
        tv.setPadding(p, p, p, p)
        tv.setTextColor(-0x1)
        tv.gravity = Gravity.CENTER
        tv.transformationMethod = AllCapsTransformationMethod(this)
        //        tv.setText("Android " + Build.VERSION.RELEASE);
        tv.setText(R.string.jpb)
        tv.visibility = View.INVISIBLE
        mContent!!.addView(bg)
        mContent!!.addView(letter, lp)
        mContent!!.addView(logo, lp)
        val lp2 = FrameLayout.LayoutParams(lp)
        lp2.gravity = Gravity.BOTTOM or Gravity.CENTER_HORIZONTAL
        lp2.bottomMargin = 10 * p
        mContent!!.addView(tv, lp2)
        mContent!!.setOnClickListener(object : View.OnClickListener {
            var clicks = 0
            override fun onClick(v: View) {
                clicks++
                if (clicks >= 10) {
                    mContent!!.performLongClick()
                    return
                }
                letter.animate().cancel()
                val offset = (letter.rotation.toInt() % 360).toFloat()
                letter.animate()
                    .rotationBy((if (Math.random() > 0.5f) 360 else -360) - offset)
                    .setInterpolator(DecelerateInterpolator())
                    .setDuration(700).start()
            }
        })
        mContent!!.setOnLongClickListener(OnLongClickListener {
            if (logo.visibility != View.VISIBLE) {
                bg.scaleX = 0.01f
                bg.animate().alpha(1f).scaleX(1f).setStartDelay(500).start()
                letter.animate().alpha(0f).scaleY(0.5f).scaleX(0.5f)
                    .rotationBy(360f)
                    .setInterpolator(AccelerateInterpolator())
                    .setDuration(1000)
                    .start()
                logo.alpha = 0f
                logo.visibility = View.VISIBLE
                logo.scaleX = 0.5f
                logo.scaleY = 0.5f
                logo.animate().alpha(1f).scaleX(1f).scaleY(1f)
                    .setDuration(1000).setStartDelay(500)
                    .setInterpolator(AnticipateOvershootInterpolator())
                    .start()
                tv.alpha = 0f
                tv.visibility = View.VISIBLE
                tv.animate().alpha(1f).setDuration(1000).setStartDelay(1000).start()
                return@OnLongClickListener true
            }
            false
        })
        logo.setOnLongClickListener { v ->
            if (v.context.getLong("k_egg_mode", 0)
                == 0L
            ) {
                // For posterity: the moment this user unlocked the easter egg
                v.context.putLong("k_egg_mode", System.currentTimeMillis())
            }
            try {
                startActivity(
                    Intent(this@PlatLogoActivity2, DessertCase::class.java)
                        .setFlags(
                            Intent.FLAG_ACTIVITY_NEW_TASK
                                    or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                    or Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS
                        )
                )
            } catch (ex: ActivityNotFoundException) {
                Log.e("PlatLogoActivity2", "Couldn't catch a break.")
            }
            finish()
            true
        }
        setContentView(mContent)
    }

    companion object {
        const val BGCOLOR = 0x7E57C2
    }
}