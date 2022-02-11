package com.imadev.foody.utils

import java.text.DecimalFormat

fun formatDecimal(value: Double): String {
    val decimalFormat = DecimalFormat("#.##")
    return decimalFormat.format(value)
}