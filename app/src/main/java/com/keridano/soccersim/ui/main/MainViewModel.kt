@file:Suppress("UNCHECKED_CAST")

package com.keridano.soccersim.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.keridano.soccersim.extension.reassign
import com.keridano.soccersim.util.DispatcherProvider
import com.keridano.soccersim.util.DispatcherProviders
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainViewModel(
    private val dispatcherProvider: DispatcherProvider = DispatcherProviders.default
) : ViewModel() {

    private var _uiState: MutableLiveData<MainViewUiState> = MutableLiveData(MainViewUiState())
    val uiState: LiveData<MainViewUiState>
        get() = _uiState

    fun startSimulation() {
        CoroutineScope(dispatcherProvider.background).launch startSimulation@{
            startLoading()
            delay(2000) //simulating an API call
            stopLoading()
        }
    }

    private fun startLoading() {
        _uiState.reassign {
            it.spinnerVisible(true).startSimulationButtonActive(false)
        }
    }

    private fun stopLoading() {
        _uiState.reassign {
            it.spinnerVisible(false).startSimulationButtonActive(true)
        }
    }
}

class MainViewModelFactory : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return MainViewModel() as T
    }
}