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

import android.animation.Animator
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
import android.os.Handler
import android.text.method.TransformationMethod
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.view.animation.DecelerateInterpolator
import android.widget.ImageView
import com.jpb.scratchtappy.md3.TransformationMethod2
import java.util.HashSet

class DessertCaseView @JvmOverloads constructor(
    context: Context?,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : FrameLayout(
    context!!, attrs, defStyle
) {
    private val mDrawables = SparseArray<Drawable>(NUM_PASTRIES)
    private var mStarted: Boolean = false
    private val mCellSize: Int
    private var mWidth = 0
    private var mHeight = 0
    private var mRows = 0
    private var mColumns = 0
    private var mCells: Array<View?>? = null
    private val mFreeList: MutableSet<Point?> = HashSet()
    private val mHandler = Handler()
    private val mJuggle: Runnable = object : Runnable {
        override fun run() {
            val N = childCount
            val K = 1 //irand(1,3);
            for (i in 0 until K) {
                val child = getChildAt((Math.random() * N).toInt())
                place(child, true)
            }
            fillFreeList()
            if (mStarted) {
                mHandler.postDelayed(this, DELAY.toLong())
            }
        }
    }

    fun start() {
        if (!mStarted) {
            mStarted = true
            fillFreeList(DURATION * 4)
        }
        mHandler.postDelayed(mJuggle, START_DELAY.toLong())
    }

    fun stop() {
        mStarted = false
        mHandler.removeCallbacks(mJuggle)
    }

    fun pick(a: IntArray): Int {
        return a[(Math.random() * a.size).toInt()]
    }

    fun <T> pick(a: Array<T>): T {
        return a[(Math.random() * a.size).toInt()]
    }

    fun <T> pick(sa: SparseArray<T>): T {
        return sa.valueAt((Math.random() * sa.size()).toInt())
    }

    var hsv = floatArrayOf(0f, 1f, .85f)
    fun random_color(): Int {
//        return 0xFF000000 | (int) (Math.random() * (float) 0xFFFFFF); // totally random
        val COLORS = 12
        hsv[0] = irand(0, COLORS) * (360f / COLORS)
        return Color.HSVToColor(hsv)
    }

    @Synchronized
    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        if (mWidth == w && mHeight == h) return
        val wasStarted = mStarted
        if (wasStarted) {
            stop()
        }
        mWidth = w
        mHeight = h
        mCells = null
        removeAllViewsInLayout()
        mFreeList.clear()
        mRows = mHeight / mCellSize
        mColumns = mWidth / mCellSize
        mCells = arrayOfNulls(mRows * mColumns)
        if (DEBUG) Log.v(TAG, String.format("New dimensions: %dx%d", mColumns, mRows))
        scaleX = SCALE
        scaleY = SCALE
        translationX = 0.5f * (mWidth - mCellSize * mColumns) * SCALE
        translationY = 0.5f * (mHeight - mCellSize * mRows) * SCALE
        for (j in 0 until mRows) {
            for (i in 0 until mColumns) {
                mFreeList.add(Point(i, j))
            }
        }
        if (wasStarted) {
            start()
        }
    }

    fun fillFreeList() {
        fillFreeList(DURATION)
    }

    @Synchronized
    fun fillFreeList(animationLen: Int) {
        val ctx = context
        val lp = LayoutParams(mCellSize, mCellSize)
        while (!mFreeList.isEmpty()) {
            val pt = mFreeList.iterator().next()
            mFreeList.remove(pt)
            val i = pt!!.x
            val j = pt.y
            if (mCells!![j * mColumns + i] != null) continue
            val v = ImageView(ctx)
            v.setOnClickListener {
                place(v, true)
                postDelayed({ fillFreeList() }, (DURATION / 2).toLong())
            }
            val c = random_color()
            v.setBackgroundColor(c)
            val which = frand()
            val d: Drawable?
            d = if (which < 0.0005f) {
                mDrawables[pick(XXRARE_PASTRIES)]
            } else if (which < 0.005f) {
                mDrawables[pick(XRARE_PASTRIES)]
            } else if (which < 0.5f) {
                mDrawables[pick(RARE_PASTRIES)]
            } else if (which < 0.7f) {
                mDrawables[pick(PASTRIES)]
            } else {
                null
            }
            if (d != null) {
                v.overlay.add(d)
            }
            lp.height = mCellSize
            lp.width = lp.height
            addView(v, lp)
            place(v, pt, false)
            if (animationLen > 0) {
                val s: Float = (v.getTag(TAG_SPAN).toString()).toFloat()
                v.scaleX = 0.5f * s
                v.scaleY = 0.5f * s
                v.alpha = 0f
                v.animate().withLayer().scaleX(s).scaleY(s).alpha(1f).duration =
                    animationLen.toLong()
            }
        }
    }

    fun place(v: View, animate: Boolean) {
        place(v, Point(irand(0, mColumns), irand(0, mRows)), animate)
    }

    // we don't have .withLayer() on general Animators
    private fun makeHardwareLayerListener(v: View): Animator.AnimatorListener {
        return object : AnimatorListenerAdapter() {
            override fun onAnimationStart(animator: Animator) {
                v.setLayerType(LAYER_TYPE_HARDWARE, null)
                v.buildLayer()
            }

            override fun onAnimationEnd(animator: Animator) {
                v.setLayerType(LAYER_TYPE_NONE, null)
            }
        }
    }

    private val tmpSet = HashSet<View>()
    @Synchronized
    fun place(v: View, pt: Point?, animate: Boolean) {
        val i = pt!!.x
        val j = pt.y
        val rnd = frand()
        if (v.getTag(TAG_POS) != null) {
            for (oc in getOccupied(v)) {
                mFreeList.add(oc)
                mCells!![oc!!.y * mColumns + oc.x] = null
            }
        }
        var scale = 1
        if (rnd < PROB_4X) {
            if (!(i >= mColumns - 3 || j >= mRows - 3)) {
                scale = 4
            }
        } else if (rnd < PROB_3X) {
            if (!(i >= mColumns - 2 || j >= mRows - 2)) {
                scale = 3
            }
        } else if (rnd < PROB_2X) {
            if (!(i == mColumns - 1 || j == mRows - 1)) {
                scale = 2
            }
        }
        v.setTag(TAG_POS, pt)
        v.setTag(TAG_SPAN, scale)
        tmpSet.clear()
        val occupied = getOccupied(v)
        for (oc in occupied) {
            val squatter = mCells!![oc!!.y * mColumns + oc.x]
            if (squatter != null) {
                tmpSet.add(squatter)
            }
        }
        for (squatter in tmpSet) {
            for (sq in getOccupied(squatter)) {
                mFreeList.add(sq)
                mCells!![sq!!.y * mColumns + sq.x] = null
            }
            if (squatter !== v) {
                squatter.setTag(TAG_POS, null)
                if (animate) {
                    squatter.animate().withLayer()
                        .scaleX(0.5f).scaleY(0.5f).alpha(0f)
                        .setDuration(DURATION.toLong())
                        .setInterpolator(AccelerateInterpolator())
                        .setListener(object : Animator.AnimatorListener {
                            override fun onAnimationStart(animator: Animator) {}
                            override fun onAnimationEnd(animator: Animator) {
                                removeView(squatter)
                            }

                            override fun onAnimationCancel(animator: Animator) {}
                            override fun onAnimationRepeat(animator: Animator) {}
                        })
                        .start()
                } else {
                    removeView(squatter)
                }
            }
        }
        for (oc in occupied) {
            mCells!![oc!!.y * mColumns + oc.x] = v
            mFreeList.remove(oc)
        }
        val rot = irand(0, 4).toFloat() * 90f
        if (animate) {
            v.bringToFront()
            val set1 = AnimatorSet()
            set1.playTogether(
                ObjectAnimator.ofFloat(v, SCALE_X, scale.toFloat()),
                ObjectAnimator.ofFloat(v, SCALE_Y, scale.toFloat())
            )
            set1.interpolator = AnticipateOvershootInterpolator()
            set1.duration = DURATION.toLong()
            val set2 = AnimatorSet()
            set2.playTogether(
                ObjectAnimator.ofFloat(v, ROTATION, rot),
                ObjectAnimator.ofFloat(
                    v,
                    X,
                    (i * mCellSize + (scale - 1) * mCellSize / 2).toFloat()
                ),
                ObjectAnimator.ofFloat(
                    v,
                    Y,
                    (j * mCellSize + (scale - 1) * mCellSize / 2).toFloat()
                )
            )
            set2.interpolator = DecelerateInterpolator()
            set2.duration = DURATION.toLong()
            set1.addListener(makeHardwareLayerListener(v))
            set1.start()
            set2.start()
        } else {
            v.x = (i * mCellSize + (scale - 1) * mCellSize / 2).toFloat()
            v.y = (j * mCellSize + (scale - 1) * mCellSize / 2).toFloat()
            v.scaleX = scale.toFloat()
            v.scaleY = scale.toFloat()
            v.rotation = rot
        }
    }

    private fun getOccupied(v: View): Array<Point?> {
        val scale = v.getTag(TAG_SPAN) as Int
        val pt = v.getTag(TAG_POS) as Point
        if (pt == null || scale == 0) return arrayOfNulls(0)
        val result = arrayOfNulls<Point>(scale * scale)
        var p = 0
        for (i in 0 until scale) {
            for (j in 0 until scale) {
                result[p++] = Point(pt.x + i, pt.y + j)
            }
        }
        return result
    }

    public override fun onDraw(c: Canvas) {
        super.onDraw(c)
        if (!DEBUG) return
        val pt = Paint(Paint.ANTI_ALIAS_FLAG)
        pt.style = Paint.Style.STROKE
        pt.color = -0x333334
        pt.strokeWidth = 2.0f
        val check = Rect()
        val N = childCount
        for (i in 0 until N) {
            val stone = getChildAt(i)
            stone.getHitRect(check)
            c.drawRect(check, pt)
        }
    }

    class RescalingContainer(context: Context?) : FrameLayout(
        context!!
    ) {
        private var mView: DessertCaseView? = null
        private var mDarkness = 0f
        fun setView(v: DessertCaseView?) {
            addView(v)
            mView = v
        }

        override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
            val w = (right - left).toFloat()
            val h = (bottom - top).toFloat()
            val w2 = (w / SCALE / 2).toInt()
            val h2 = (h / SCALE / 2).toInt()
            val cx = (left + w * 0.5f).toInt()
            val cy = (top + h * 0.5f).toInt()
            mView!!.layout(cx - w2, cy - h2, cx + w2, cy + h2)
        }

        var darkness: Float
            get() = mDarkness
            set(p) {
                mDarkness = p
                darkness
                val x = (p * 0xff).toInt()
                setBackgroundColor(x shl 24 and -0x1000000)
            }

        init {
            systemUiVisibility = (0
                    or SYSTEM_UI_FLAG_FULLSCREEN
                    or SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    or SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    or SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    or SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    or SYSTEM_UI_FLAG_IMMERSIVE_STICKY)
        }
    }

    companion object {
        private val TAG = DessertCaseView::class.java.simpleName
        private const val DEBUG = false
        const val START_DELAY = 5000
        const val DELAY = 2000
        const val DURATION = 500
        private const val TAG_POS = 0x2000001
        private const val TAG_SPAN = 0x2000002
        private val PASTRIES = intArrayOf( // used with permission
            R.drawable.dessert_android,
            R.drawable.jpbegg,
            R.drawable.categg,
            R.drawable.amogusegg
        )
        private val RARE_PASTRIES = intArrayOf( // 2010
            R.drawable.ytegg,  // 2011
            R.drawable.dessert_keylimepie,  // 2011
            R.drawable.dessert_jellybean,
            R.drawable.mdegg
        )
        private val XRARE_PASTRIES = intArrayOf( // the original and still delicious
            R.drawable.dessert_zombiegingerbread,  // remember kids, this was long before cronuts
            R.drawable.dessert_jandycane//     sholes final approach
            //     landing gear punted to flan
            //     runway foam glistens
            //         -- mcleron

        )
        private val XXRARE_PASTRIES = intArrayOf(
            R.drawable.phoneegg
        )
        private val NUM_PASTRIES = (PASTRIES.size + RARE_PASTRIES.size
                + XRARE_PASTRIES.size + XXRARE_PASTRIES.size)
        private val MASK = floatArrayOf(
            0f, 0f, 0f, 0f, 255f,
            0f, 0f, 0f, 0f, 255f,
            0f, 0f, 0f, 0f, 255f,
            1f, 0f, 0f, 0f, 0f
        )
        private val ALPHA_MASK = floatArrayOf(
            0f, 0f, 0f, 0f, 255f,
            0f, 0f, 0f, 0f, 255f,
            0f, 0f, 0f, 0f, 255f,
            0f, 0f, 0f, 1f, 0f
        )
        private val WHITE_MASK = floatArrayOf(
            0f, 0f, 0f, 0f, 255f,
            0f, 0f, 0f, 0f, 255f,
            0f, 0f, 0f, 0f, 255f,
            -1f, 0f, 0f, 0f, 255f
        )
        const val SCALE = 0.25f // natural display size will be SCALE*mCellSize
        private const val PROB_2X = 0.33f
        private const val PROB_3X = 0.1f
        private const val PROB_4X = 0.01f
        private fun convertToAlphaMask(b: Bitmap?): Bitmap {
            val a = Bitmap.createBitmap(b!!.width, b.height, Bitmap.Config.ALPHA_8)
            val c = Canvas(a)
            val pt = Paint(Paint.ANTI_ALIAS_FLAG)
            pt.colorFilter = ColorMatrixColorFilter(MASK)
            c.drawBitmap(b, 0.0f, 0.0f, pt)
            return a
        }

        fun frand(): Float {
            return Math.random().toFloat()
        }

        fun frand(a: Float, b: Float): Float {
            return frand() * (b - a) + a
        }

        fun irand(a: Int, b: Int): Int {
            return frand(a.toFloat(), b.toFloat()).toInt()
        }
    }

    init {
        val res = resources
        mStarted = false
        mCellSize = res.getDimensionPixelSize(R.dimen.dessert_case_cell_size)
        val opts = BitmapFactory.Options()
        if (mCellSize < 512) { // assuming 512x512 images
            opts.inSampleSize = 2
        }
        opts.inMutable = true
        var loaded: Bitmap? = null
        for (list in arrayOf(PASTRIES, RARE_PASTRIES, XRARE_PASTRIES, XXRARE_PASTRIES)) {
            for (resid in list) {
                opts.inBitmap = loaded
                loaded = BitmapFactory.decodeResource(res, resid, opts)
                val d = BitmapDrawable(res, convertToAlphaMask(loaded))
                d.colorFilter = ColorMatrixColorFilter(ALPHA_MASK)
                d.setBounds(0, 0, mCellSize, mCellSize)
                mDrawables.append(resid, d)
            }
        }
        loaded = null
        if (DEBUG) setWillNotDraw(false)
    }
}