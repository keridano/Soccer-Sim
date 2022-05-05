package com.keridano.soccersim.extension

import androidx.lifecycle.MutableLiveData

inline fun <T> MutableLiveData<T>?.reassign(mapper: (T) -> (T)) {
    this?.value?.let { v ->
        this.postValue(mapper(v))
    }
}