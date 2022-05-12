package com.keridano.soccersim.extension

import androidx.lifecycle.MutableLiveData

/**
 * Extension used to ensure null safety when posting MutableLiveData values
 */
inline fun <T> MutableLiveData<T>?.reassign(mapper: (T) -> (T)) {
    this?.value?.let { v ->
        this.postValue(mapper(v))
    }
}