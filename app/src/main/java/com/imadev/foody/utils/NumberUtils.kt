package com.imadev.foody.utils

import android.util.Log
import java.text.DecimalFormat
import kotlin.math.log

private const val TAG = "NumberUtils"
fun formatDecimal(value: Double): String {
    val decimalFormat = DecimalFormat("#.##")
    return decimalFormat.format(value)
}