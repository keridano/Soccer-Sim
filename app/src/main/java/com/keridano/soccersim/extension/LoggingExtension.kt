package com.keridano.soccersim.extension

import java.lang.Integer.min

val Any.TAG: String
    get() = this.javaClass.simpleName.let { it.substring(0, min(23, it.length)) }