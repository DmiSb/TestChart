package ru.dmisb.testchart.utils

import android.content.Context
import android.util.TypedValue
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat

fun Context.dpToPx(value: Int) : Int =
    TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        value.toFloat(),
        this.resources.displayMetrics
    ).toInt()

fun Context.color(@ColorRes colorRes: Int) : Int =
    ContextCompat.getColor(this, colorRes)