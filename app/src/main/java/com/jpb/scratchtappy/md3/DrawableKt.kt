@file:JvmName("DrawableKt")

package com.jpb.scratchtappy.usp

import android.content.Context
import androidx.core.content.ContextCompat

fun Context.getSystemColor(resName: String): Int {
    val id = resources.getIdentifier(resName, "color", "android")
    return ContextCompat.getColor(this, id)
}
