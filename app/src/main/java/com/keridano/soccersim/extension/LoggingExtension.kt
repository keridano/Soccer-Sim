package com.keridano.soccersim.extension

import java.lang.Integer.min

/**
 * Extension used to make sure that a correct TAG is used when logging a class
 */
val Any.TAG: String
    get() = this.javaClass.simpleName.let { it.substring(0, min(23, it.length)) }

/**
 * Extension useful to pretty print classes in logcat
 */
fun Any.prettyPrint(): String {

    var indentLevel = 0
    val indentWidth = 4

    fun padding() = "".padStart(indentLevel * indentWidth)

    val toString = toString()

    val stringBuilder = StringBuilder(toString.length)

    var i = 0
    while (i < toString.length) {
        when (val char = toString[i]) {
            '(', '[', '{' -> {
                indentLevel++
                stringBuilder.appendLine(char).append(padding())
            }
            ')', ']', '}' -> {
                indentLevel--
                stringBuilder.appendLine().append(padding()).append(char)
            }
            ',' -> {
                stringBuilder.appendLine(char).append(padding())
                // ignore space after comma as we have added a newline
                val nextChar = toString.getOrElse(i + 1) { char }
                if (nextChar == ' ') i++
            }
            else -> {
                stringBuilder.append(char)
            }
        }
        i++
    }

    return stringBuilder.toString().replace("=", " = ")
}