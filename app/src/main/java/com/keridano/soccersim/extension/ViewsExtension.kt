package com.keridano.soccersim.extension

import android.view.View

@Suppress("unused")
fun View.isVisible(): Boolean = this.visibility == View.VISIBLE

fun View.visible(visible: Boolean = true) {
    visibility = if (visible) View.VISIBLE else View.GONE
}