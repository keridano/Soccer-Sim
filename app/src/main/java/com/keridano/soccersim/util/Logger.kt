package com.keridano.soccersim.util

import android.util.Log

/**
 * Utility to simplify logging without the need to always specify a TAG in every Log message
 */
@Suppress("unused")
class Logger(private val tag: String) {
    fun d(msg: String) = Log.d(tag, msg)
    fun w(msg: String) = Log.w(tag, msg)
    fun e(msg: String) = Log.e(tag, msg)
}