/*
 * Copyright (C) 2011 The Android Open Source Project
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
import android.graphics.drawable.BitmapDrawable
import android.service.dreams.DreamService
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.Gravity
import com.jpb.scratchtappy.md3.PlatLogoActivity2
import android.widget.TextView
import android.view.View.OnLongClickListener
import android.content.Intent
import com.jpb.scratchtappy.md3.DessertCase
import android.content.ActivityNotFoundException
import android.content.Context
import android.graphics.*
import android.text.method.TransformationMethod
import android.util.Log
import android.view.View
import android.widget.Toast
import com.jpb.scratchtappy.md3.TransformationMethod2
import java.util.*
import androidx.core.content.ContextCompat

/**
 * Transforms source text into an ALL CAPS string, locale-aware.
 */
class AllCapsTransformationMethod(context: Context) : TransformationMethod2 {
    private var mEnabled = false
    private val mLocale: Locale
    override fun getTransformation(source: CharSequence, view: View): CharSequence {
        if (mEnabled) {
            return if (source != null) source.toString().toUpperCase(mLocale) else source.toString()
        }
        val text = "Caller did not enable length changes; not transforming text"
        val duration = Toast.LENGTH_LONG

        return source
    }

    override fun onFocusChanged(
        view: View, sourceText: CharSequence, focused: Boolean, direction: Int,
        previouslyFocusedRect: Rect
    ) {
    }

    override fun setLengthChangesAllowed(allowLengthChanges: Boolean) {
        mEnabled = allowLengthChanges
    }

    companion object {
        private const val TAG = "AllCapsTransformationMethod"
    }

    init {
        mLocale = context.resources.configuration.locale
    }
}