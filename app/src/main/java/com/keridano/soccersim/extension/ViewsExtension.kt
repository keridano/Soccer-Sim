package com.keridano.soccersim.extension

import android.view.View

fun View.isVisible(): Boolean = this.visibility == View.VISIBLE

fun View.visible(visible: Boolean = true) {
    visibility = if (visible) View.VISIBLE else View.GONE
}