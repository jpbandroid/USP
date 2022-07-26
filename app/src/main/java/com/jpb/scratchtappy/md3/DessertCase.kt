/*
 * Copyright (C) 2013 The Android Open Source Project
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
import android.text.method.TransformationMethod
import com.jpb.scratchtappy.md3.TransformationMethod2

class DessertCase : Activity() {
    var mView: DessertCaseView? = null
    public override fun onStart() {
        super.onStart()
        val pm = packageManager
        val cn = ComponentName(this, DessertCaseDream::class.java)
        if (pm.getComponentEnabledSetting(cn) != PackageManager.COMPONENT_ENABLED_STATE_ENABLED) {
//            Slog.v("DessertCase", "ACHIEVEMENT UNLOCKED");
            pm.setComponentEnabledSetting(
                cn,
                PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                PackageManager.DONT_KILL_APP
            )
        }
        mView = DessertCaseView(this)
        val container = RescalingContainer(this)
        container.setView(mView)
        setContentView(container)
    }

    public override fun onResume() {
        super.onResume()
        mView!!.postDelayed({ mView!!.start() }, 1000)
    }

    public override fun onPause() {
        super.onPause()
        mView!!.stop()
    }
}