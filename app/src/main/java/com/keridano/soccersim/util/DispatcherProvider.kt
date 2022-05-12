package com.keridano.soccersim.util

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

/**
 * Utility interface to standardise the [CoroutineDispatcher]s to use in the app
 */
interface DispatcherProvider {
    val background: CoroutineDispatcher
    val main: CoroutineDispatcher
}

class DefaultDispatcherProvider : DispatcherProvider {
    override val background: CoroutineDispatcher
        get() = Dispatchers.Default

    override val main: CoroutineDispatcher
        get() = Dispatchers.Main
}

object DispatcherProviders {
    val default = DefaultDispatcherProvider()
}