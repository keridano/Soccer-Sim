@file:Suppress("UNCHECKED_CAST")

package com.keridano.soccersim.ui.main

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class MainViewModel : ViewModel() {
    // TODO: Implement the ViewModel
}

//TODO: see if context is not needed
class MainViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return MainViewModel() as T
    }
}