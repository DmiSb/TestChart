package ru.dmisb.testchart.utils

import android.content.Context
import android.util.TypedValue
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat

fun Context.dpToPx(value: Float) : Float =
    TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        value,
        this.resources.displayMetrics
    )

fun Context.color(@ColorRes colorRes: Int) : Int =
    ContextCompat.getColor(this, colorRes)