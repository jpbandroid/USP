/*
 * Copyright (C) 2020 The Android Open Source Project
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

import com.jpb.scratchtappy.md3.getSystemColor
import kotlin.jvm.JvmOverloads
import android.graphics.drawable.Drawable
import android.graphics.drawable.VectorDrawable
import android.content.IntentFilter
import android.content.Intent
import android.view.View.MeasureSpec
import android.content.BroadcastReceiver
import android.text.format.DateUtils
import android.content.res.TypedArray
import com.jpb.scratchtappy.md3.R
import androidx.core.content.ContextCompat
import android.app.Activity
import com.jpb.scratchtappy.md3.PlatLogoActivity.SettableAnalogClock
import com.jpb.scratchtappy.md3.PlatLogoActivity.BubblesDrawable
import android.os.Bundle
import android.widget.FrameLayout
import android.util.DisplayMetrics
import android.view.Gravity
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.LayerDrawable
import android.view.animation.OvershootInterpolator
import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.view.MotionEvent
import android.view.HapticFeedbackConstants
import com.jpb.scratchtappy.md3.PlatLogoActivity
import com.jpb.scratchtappy.md3.PlatLogoActivity.Bubble
import android.text.format.Time
import android.util.Log
import android.view.View
import android.view.animation.DecelerateInterpolator
import android.widget.ImageView
import android.widget.Toast
import com.google.android.material.color.DynamicColors
import java.lang.Exception

/**
 * Android S
 */
class PlatLogoActivity : Activity() {
    private val MAX_BUBBS = 2000
    //    private static final String S_EGG_UNLOCK_SETTING = "egg_mode_s";
    private var mClock: SettableAnalogClock? = null
    private var mLogo: ImageView? = null
    private var mBg: BubblesDrawable? = null

    //    @Override
    //    protected void onPause() {
    //        super.onPause();
    //    }
    override fun onCreate(savedInstanceState: Bundle?) {
        DynamicColors.applyIfAvailable(this);
        super.onCreate(savedInstanceState)
        window.navigationBarColor = 0
        window.statusBarColor = 0
        val ab = actionBar
        ab?.hide()
        val layout = FrameLayout(this)
        mClock = SettableAnalogClock(this)
        val dm = resources.displayMetrics
        val dp = dm.density
        val minSide = Math.min(dm.widthPixels, dm.heightPixels)
        val widgetSize = (minSide * 0.75).toInt()
        val lp = FrameLayout.LayoutParams(widgetSize, widgetSize)
        lp.gravity = Gravity.CENTER
        layout.addView(mClock, lp)
        mLogo = ImageView(this)
        mLogo!!.visibility = View.GONE
        //        mLogo.setImageResource(R.drawable.s_platlogo);
        mLogo!!.setImageDrawable(createDrawable())
        layout.addView(mLogo, lp)
        mBg = BubblesDrawable()
        mBg!!.level = 0
        mBg!!.avoid = (widgetSize / 2).toFloat()
        mBg!!.padding = 0.5f * dp
        mBg!!.minR = 1 * dp
        layout.background = mBg
        setContentView(layout)
    }

    private fun createDrawable(): Drawable? {
        var color = -1
        try {
            color = this.getSystemColor("system_accent3_500")
        } catch (ignore: Exception) {
        }
        if (color != -1) {
            color = R.color.purple_500
            val drawable = ContextCompat.getDrawable(this, R.drawable.s_platlogo_nobg)
            val bg = GradientDrawable()
            bg.shape = GradientDrawable.OVAL
            bg.setColor(color)
            return LayerDrawable(arrayOf(bg, drawable))
        }
        return ContextCompat.getDrawable(this, R.drawable.s_platlogo)
    }

    //    private boolean shouldWriteSettings() {
    //        return getPackageName().equals("android");
    //    }
    @SuppressLint("ObjectAnimatorBinding")
    private fun launchNextStage(locked: Boolean) {
        mClock!!.animate()
            .alpha(0f).scaleX(0.5f).scaleY(0.5f)
            .withEndAction { mClock!!.visibility = View.GONE }
            .start()
        mLogo!!.alpha = 0f
        mLogo!!.scaleX = 0.5f
        mLogo!!.scaleY = 0.5f
        mLogo!!.visibility = View.VISIBLE
        mLogo!!.animate()
            .alpha(1f)
            .scaleX(1f)
            .scaleY(1f)
            .setInterpolator(OvershootInterpolator())
            .start()
        mLogo!!.postDelayed(
            {
                val anim = ObjectAnimator.ofInt(mBg, "level", 0, 10000)
                anim.interpolator = DecelerateInterpolator(1f)
                anim.start()
            },
            500
        )

//        final ContentResolver cr = getContentResolver();
//
//        try {
//            if (shouldWriteSettings()) {
//                Log.v(TAG, "Saving egg unlock=" + locked);
//                syncTouchPressure();
//                Settings.System.putLong(cr,
//                        S_EGG_UNLOCK_SETTING,
//                        locked ? 0 : System.currentTimeMillis());
//            }
//        } catch (RuntimeException e) {
//            Log.e(TAG, "Can't write settings", e);
//        }
//
//        try {
//            startActivity(new Intent(Intent.ACTION_MAIN)
//                    .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
//                            | Intent.FLAG_ACTIVITY_CLEAR_TASK)
//                    .addCategory("com.android.internal.category.PLATLOGO"));
//        } catch (ActivityNotFoundException ex) {
//            Log.e("com.android.internal.app.PlatLogoActivity", "No more eggs.");
//        }
        //finish(); // no longer finish upon unlock; it's fun to frob the dial
    }
    //    static final String TOUCH_STATS = "touch.stats";
    //    double mPressureMin = 0, mPressureMax = -1;
    //
    //    private void measureTouchPressure(MotionEvent event) {
    //        final float pressure = event.getPressure();
    //        switch (event.getActionMasked()) {
    //            case MotionEvent.ACTION_DOWN:
    //                if (mPressureMax < 0) {
    //                    mPressureMin = mPressureMax = pressure;
    //                }
    //                break;
    //            case MotionEvent.ACTION_MOVE:
    //                if (pressure < mPressureMin) mPressureMin = pressure;
    //                if (pressure > mPressureMax) mPressureMax = pressure;
    //                break;
    //        }
    //    }
    //
    //    private void syncTouchPressure() {
    //        try {
    //            final String touchDataJson = Settings.System.getString(
    //                    getContentResolver(), TOUCH_STATS);
    //            final JSONObject touchData = new JSONObject(
    //                    touchDataJson != null ? touchDataJson : "{}");
    //            if (touchData.has("min")) {
    //                mPressureMin = Math.min(mPressureMin, touchData.getDouble("min"));
    //            }
    //            if (touchData.has("max")) {
    //                mPressureMax = Math.max(mPressureMax, touchData.getDouble("max"));
    //            }
    //            if (mPressureMax >= 0) {
    //                touchData.put("min", mPressureMin);
    //                touchData.put("max", mPressureMax);
    //                if (shouldWriteSettings()) {
    //                    Settings.System.putString(getContentResolver(), TOUCH_STATS,
    //                            touchData.toString());
    //                }
    //            }
    //        } catch (Exception e) {
    //            Log.e(TAG, "Can't write touch settings", e);
    //        }
    //    }
    //
    //    @Override
    //    public void onStart() {
    //        super.onStart();
    //        syncTouchPressure();
    //    }
    //
    //    @Override
    //    public void onStop() {
    //        syncTouchPressure();
    //        super.onStop();
    //    }
    /**
     * Subclass of AnalogClock that allows the user to flip up the glass and adjust the hands.
     */
    inner class SettableAnalogClock(context: Context) : AnalogClock(context) {
        private var mOverrideHour = -1
        private var mOverrideMinute = -1
        private var mOverride = false
        public override fun now(): Time? {
            val realNow = super.now()
            //            final Instant realNow = super.now();
//            final ZoneId tz = Clock.systemDefaultZone().getZone();
//            final ZonedDateTime zdTime = realNow.atZone(tz);
            if (mOverride) {
                if (mOverrideHour < 0) {
//                    mOverrideHour = zdTime.getHour();
                    mOverrideHour = realNow!!.hour
                }
                realNow!![0, mOverrideMinute, mOverrideHour, realNow.monthDay, realNow.month] =
                    realNow.year
                //                return Clock.fixed(zdTime
//                        .withHour(mOverrideHour)
//                        .withMinute(mOverrideMinute)
//                        .withSecond(0)
//                        .toInstant(), tz).instant();
//            } else {
//                return realNow;
            }
            return realNow
        }

        fun toPositiveDegrees(rad: Double): Double {
            return (Math.toDegrees(rad) + 360 - 90) % 360
        }

        @SuppressLint("ClickableViewAccessibility")
        override fun onTouchEvent(ev: MotionEvent): Boolean {
            when (ev.actionMasked) {
                MotionEvent.ACTION_DOWN -> {
                    mOverride = true
                    //                    measureTouchPressure(ev);
                    val x = ev.x
                    val y = ev.y
                    val cx = width / 2f
                    val cy = height / 2f
                    val angle = toPositiveDegrees(
                        Math.atan2(
                            (x - cx).toDouble(),
                            (y - cy).toDouble()
                        )
                    ).toFloat()
                    val minutes = (75 - (angle / 6).toInt()) % 60
                    val minuteDelta = minutes - mOverrideMinute
                    if (minuteDelta != 0) {
                        if (Math.abs(minuteDelta) > 45 && mOverrideHour >= 0) {
                            val hourDelta = if (minuteDelta < 0) 1 else -1
                            mOverrideHour = (mOverrideHour + 24 + hourDelta) % 24
                        }
                        mOverrideMinute = minutes
                        if (mOverrideMinute == 0) {
                            performHapticFeedback(HapticFeedbackConstants.LONG_PRESS)
                            if (scaleX == 1f) {
                                scaleX = 1.05f
                                scaleY = 1.05f
                                animate().scaleX(1f).scaleY(1f).setDuration(150).start()
                            }
                        } else {
                            performHapticFeedback(HapticFeedbackConstants.CLOCK_TICK)
                        }
                        onTimeChanged()
                        postInvalidate()
                    }
                    return true
                }
                MotionEvent.ACTION_MOVE -> {
                    val x = ev.x
                    val y = ev.y
                    val cx = width / 2f
                    val cy = height / 2f
                    val angle = toPositiveDegrees(
                        Math.atan2(
                            (x - cx).toDouble(),
                            (y - cy).toDouble()
                        )
                    ).toFloat()
                    val minutes = (75 - (angle / 6).toInt()) % 60
                    val minuteDelta = minutes - mOverrideMinute
                    if (minuteDelta != 0) {
                        if (Math.abs(minuteDelta) > 45 && mOverrideHour >= 0) {
                            val hourDelta = if (minuteDelta < 0) 1 else -1
                            mOverrideHour = (mOverrideHour + 24 + hourDelta) % 24
                        }
                        mOverrideMinute = minutes
                        if (mOverrideMinute == 0) {
                            performHapticFeedback(HapticFeedbackConstants.LONG_PRESS)
                            if (scaleX == 1f) {
                                scaleX = 1.05f
                                scaleY = 1.05f
                                animate().scaleX(1f).scaleY(1f).setDuration(150).start()
                            }
                        } else {
                            performHapticFeedback(HapticFeedbackConstants.CLOCK_TICK)
                        }
                        onTimeChanged()
                        postInvalidate()
                    }
                    return true
                }
                MotionEvent.ACTION_UP -> {
                    if (mOverrideMinute == 0 && mOverrideHour % 12 == 0) {
                        val text = "12:00 let's gooooo"
                        val duration = Toast.LENGTH_LONG
                        val toast = Toast.makeText(applicationContext, text, duration)
                        toast.show()
                        Log.v(TAG, "12:00 let's gooooo")
                        performHapticFeedback(HapticFeedbackConstants.LONG_PRESS)
                        launchNextStage(false)
                    }
                    return true
                }
            }
            return false
        }
    }

    internal class Bubble {
        var x = 0f
        var y = 0f
        var r = 0f
        var color = 0
    }

    internal inner class BubblesDrawable : Drawable() {
        //        private final int[] mColorIds = {
        //                android.R.color.system_accent1_400,
        //                android.R.color.system_accent1_500,
        //                android.R.color.system_accent1_600,
        //
        //                android.R.color.system_accent2_400,
        //                android.R.color.system_accent2_500,
        //                android.R.color.system_accent2_600,
        //        };
        private val mColorIds = arrayOf(
            "system_accent1_400",
            "system_accent1_500",
            "system_accent1_600",
            "system_accent2_400",
            "system_accent2_500",
            "system_accent2_600"
        )

        //        private int[] mColors = new int[mColorIds.length];
        private val mColors =
            intArrayOf(-0xa67209, -0xc88e21, -0xdaa644, -0x756e5d, -0x8f8979, -0xa7a191)
        private val mBubbs = arrayOfNulls<Bubble>(MAX_BUBBS)
        private var mNumBubbs = 0
        private val mPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        var avoid = 0f
        var padding = 0f
        var minR = 0f
        override fun draw(canvas: Canvas) {
            val f = level / 10000f
            mPaint.style = Paint.Style.FILL
            var drawn = 0
            for (j in 0 until mNumBubbs) {
                if (mBubbs[j]!!.color == 0 || mBubbs[j]!!.r == 0f) continue
                mPaint.color = mBubbs[j]!!.color
                canvas.drawCircle(mBubbs[j]!!.x, mBubbs[j]!!.y, mBubbs[j]!!.r * f, mPaint)
                drawn++
            }
        }

        override fun onLevelChange(level: Int): Boolean {
            invalidateSelf()
            return true
        }

        override fun onBoundsChange(bounds: Rect) {
            super.onBoundsChange(bounds)
            randomize()
        }

        private fun randomize() {
            val w = bounds.width().toFloat()
            val h = bounds.height().toFloat()
            val maxR = Math.min(w, h) / 3f
            mNumBubbs = 0
            if (avoid > 0f) {
                mBubbs[mNumBubbs]!!.x = w / 2f
                mBubbs[mNumBubbs]!!.y = h / 2f
                mBubbs[mNumBubbs]!!.r = avoid
                mBubbs[mNumBubbs]!!.color = 0
                mNumBubbs++
            }
            for (j in 0 until MAX_BUBBS) {
                // a simple but time-tested bubble-packing algorithm:
                // 1. pick a spot
                // 2. shrink the bubble until it is no longer overlapping any other bubble
                // 3. if the bubble hasn't popped, keep it
                var tries = 5
                while (tries-- > 0) {
                    val x = Math.random().toFloat() * w
                    val y = Math.random().toFloat() * h
                    var r = Math.min(Math.min(x, w - x), Math.min(y, h - y))

                    // shrink radius to fit other bubbs
                    for (i in 0 until mNumBubbs) {
                        r = Math.min(
                            r.toDouble(),
                            Math.hypot(
                                (x - mBubbs[i]!!.x).toDouble(),
                                (y - mBubbs[i]!!.y).toDouble()
                            ) - mBubbs[i]!!.r
                                    - padding
                        ).toFloat()
                        if (r < minR) break
                    }
                    if (r >= minR) {
                        // we have found a spot for this bubble to live, let's save it and move on
                        r = Math.min(maxR, r)
                        mBubbs[mNumBubbs]!!.x = x
                        mBubbs[mNumBubbs]!!.y = y
                        mBubbs[mNumBubbs]!!.r = r
                        mBubbs[mNumBubbs]!!.color = mColors[(Math.random() * mColors.size).toInt()]
                        mNumBubbs++
                        break
                    }
                }
            }
            Log.v(
                TAG, String.format(
                    "successfully placed %d bubbles (%d%%)",
                    mNumBubbs, (100f * mNumBubbs / MAX_BUBBS).toInt()
                )
            )
        }

        override fun setAlpha(alpha: Int) {}
        override fun setColorFilter(colorFilter: ColorFilter?) {}
        override fun getOpacity(): Int {
            return PixelFormat.TRANSLUCENT
        }


        init {
            try {
                for (i in mColorIds.indices) {
                    mColors[i] = this@PlatLogoActivity.getSystemColor(mColorIds[i])
                }
            } catch (ignore: Exception) {
            }
            for (j in mBubbs.indices) {
                mBubbs[j] = Bubble()
            }
        }
    }

    companion object {
        private const val TAG = "PlatLogoActivity"
    }
}