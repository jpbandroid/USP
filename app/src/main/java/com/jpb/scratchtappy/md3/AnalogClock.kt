/*
 * Copyright (C) 2006 The Android Open Source Project
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
import android.content.Context
import android.graphics.Canvas
import android.view.MotionEvent
import android.view.HapticFeedbackConstants
import com.jpb.scratchtappy.md3.PlatLogoActivity
import com.jpb.scratchtappy.md3.PlatLogoActivity.Bubble
import android.graphics.ColorFilter
import android.graphics.PixelFormat
import android.text.format.Time
import android.util.AttributeSet
import android.view.View
import java.lang.Exception
import java.util.*

/**
 * This widget display an analogic clock with two hands for hours and
 * minutes.
 *
 *
 * Copy from Framework branch android-7.1.2_r39
 *
 * @attr ref android.R.styleable#AnalogClock_dial
 * @attr ref android.R.styleable#AnalogClock_hand_hour
 * @attr ref android.R.styleable#AnalogClock_hand_minute
 */
open class AnalogClock @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    defStyleRes: Int = 0
) : View(context, attrs, defStyleAttr, defStyleRes) {
    private var mCalendar: Time
    private var mHourHand: Drawable?
    private var mMinuteHand: Drawable?
    private var mDial: Drawable?
    private val mDialWidth: Int
    private val mDialHeight: Int
    private var mAttached = false
    private var mMinutes = 0f
    private var mHour = 0f
    private var mChanged = false
    private fun setDrawableTint(drawable: VectorDrawable?, tintColor: String) {
        if (drawable == null) return
        try {
            val systemColor = context.getSystemColor(tintColor)
            drawable.setTint(systemColor)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        if (!mAttached) {
            mAttached = true
            val filter = IntentFilter()
            filter.addAction(Intent.ACTION_TIME_TICK)
            filter.addAction(Intent.ACTION_TIME_CHANGED)
            filter.addAction(Intent.ACTION_TIMEZONE_CHANGED)

            // OK, this is gross but needed. This class is supported by the
            // remote views machanism and as a part of that the remote views
            // can be inflated by a context for another user without the app
            // having interact users permission - just for loading resources.
            // For exmaple, when adding widgets from a user profile to the
            // home screen. Therefore, we register the receiver as the current
            // user not the one the context is for.
            context.registerReceiver(
                mIntentReceiver,
                filter, null, handler
            )
        }

        // NOTE: It's safe to do these after registering the receiver since the receiver always runs
        // in the main thread, therefore the receiver can't run before this method returns.

        // The time zone may have changed while the receiver wasn't registered, so update the Time
        mCalendar = Time()

        // Make sure we update to the current time
        onTimeChanged()
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        if (mAttached) {
            context.unregisterReceiver(mIntentReceiver)
            mAttached = false
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val widthMode = MeasureSpec.getMode(widthMeasureSpec)
        val widthSize = MeasureSpec.getSize(widthMeasureSpec)
        val heightMode = MeasureSpec.getMode(heightMeasureSpec)
        val heightSize = MeasureSpec.getSize(heightMeasureSpec)
        var hScale = 1.0f
        var vScale = 1.0f
        if (widthMode != MeasureSpec.UNSPECIFIED && widthSize < mDialWidth) {
            hScale = widthSize.toFloat() / mDialWidth.toFloat()
        }
        if (heightMode != MeasureSpec.UNSPECIFIED && heightSize < mDialHeight) {
            vScale = heightSize.toFloat() / mDialHeight.toFloat()
        }
        val scale = Math.min(hScale, vScale)
        setMeasuredDimension(
            resolveSizeAndState((mDialWidth * scale).toInt(), widthMeasureSpec, 0),
            resolveSizeAndState((mDialHeight * scale).toInt(), heightMeasureSpec, 0)
        )
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        mChanged = true
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val changed = mChanged
        if (changed) {
            mChanged = false
        }
        val availableWidth = right - left
        val availableHeight = bottom - top
        val x = availableWidth / 2
        val y = availableHeight / 2
        val dial = mDial
        var w = dial!!.intrinsicWidth
        var h = dial.intrinsicHeight
        var scaled = false
        if (availableWidth < w || availableHeight < h) {
            scaled = true
            val scale = Math.min(
                availableWidth.toFloat() / w.toFloat(),
                availableHeight.toFloat() / h.toFloat()
            )
            canvas.save()
            canvas.scale(scale, scale, x.toFloat(), y.toFloat())
        }
        if (changed) {
            dial.setBounds(x - w / 2, y - h / 2, x + w / 2, y + h / 2)
        }
        dial.draw(canvas)
        canvas.save()
        canvas.rotate(mHour / 12.0f * 360.0f, x.toFloat(), y.toFloat())
        val hourHand = mHourHand
        if (changed) {
            w = hourHand!!.intrinsicWidth
            h = hourHand.intrinsicHeight
            hourHand.setBounds(x - w / 2, y - h / 2, x + w / 2, y + h / 2)
        }
        hourHand!!.draw(canvas)
        canvas.restore()
        canvas.save()
        canvas.rotate(mMinutes / 60.0f * 360.0f, x.toFloat(), y.toFloat())
        val minuteHand = mMinuteHand
        if (changed) {
            w = minuteHand!!.intrinsicWidth
            h = minuteHand.intrinsicHeight
            minuteHand.setBounds(x - w / 2, y - h / 2, x + w / 2, y + h / 2)
        }
        minuteHand!!.draw(canvas)
        canvas.restore()
        if (scaled) {
            canvas.restore()
        }
    }

    protected open fun now(): Time? {
        mCalendar.setToNow()
        return mCalendar
    }

    protected fun onTimeChanged() {
        val now = now()
        val hour = now!!.hour
        val minute = now.minute
        val second = now.second
        mMinutes = minute + second / 60.0f
        mHour = hour + mMinutes / 60.0f
        mChanged = true
        updateContentDescription(now)
    }

    private val mIntentReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            if (intent.action == Intent.ACTION_TIMEZONE_CHANGED) {
                val tz = intent.getStringExtra("time-zone")
                mCalendar = Time(TimeZone.getTimeZone(tz).id)
            }
            onTimeChanged()
            invalidate()
        }
    }

    private fun updateContentDescription(time: Time?) {
        val flags = DateUtils.FORMAT_SHOW_TIME or DateUtils.FORMAT_24HOUR
        val contentDescription = DateUtils.formatDateTime(
            context,
            time!!.toMillis(false), flags
        )
        setContentDescription(contentDescription)
    }

    init {

        //final Resources r = context.getResources();
        val a = context.obtainStyledAttributes(
            attrs, R.styleable.AnalogClock, defStyleAttr, defStyleRes
        )
        mDial = a.getDrawable(R.styleable.AnalogClock_dial)
        if (mDial == null) {
            mDial = ContextCompat.getDrawable(context, R.drawable.clock_dial)
            setDrawableTint(mDial as VectorDrawable?, "system_neutral1_200")
        }
        mHourHand = a.getDrawable(R.styleable.AnalogClock_hand_hour)
        if (mHourHand == null) {
            mHourHand = ContextCompat.getDrawable(context, R.drawable.clock_hand_hour)
            setDrawableTint(mHourHand as VectorDrawable?, "system_accent1_700")
        }
        mMinuteHand = a.getDrawable(R.styleable.AnalogClock_hand_minute)
        if (mMinuteHand == null) {
            mMinuteHand = ContextCompat.getDrawable(context, R.drawable.clock_hand_minute)
            setDrawableTint(mMinuteHand as VectorDrawable?, "system_accent2_500")
        }
        a.recycle()
        mCalendar = Time()
        mDialWidth = mDial!!.intrinsicWidth
        mDialHeight = mDial!!.intrinsicHeight
    }
}