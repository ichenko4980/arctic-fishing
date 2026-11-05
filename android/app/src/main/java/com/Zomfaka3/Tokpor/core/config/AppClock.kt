package com.Zomfaka3.Tokpor.core.config

object AppClock {

    private const val MILLIS_PER_DAY = 86_400_000L

    fun now(): Long = System.currentTimeMillis()

    fun dayIndex(timestamp: Long = now()): Long = timestamp / MILLIS_PER_DAY
}
