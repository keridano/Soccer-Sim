package com.keridano.soccersim.extension

import android.view.View

/**
 * Extension useful to check visibility of any view
 */
@Suppress("unused")
fun View.isVisible(): Boolean = this.visibility == View.VISIBLE

/**
 * Extension useful to change visibility of any view
 */
fun View.visible(visible: Boolean = true) {
    visibility = if (visible) View.VISIBLE else View.GONE
}