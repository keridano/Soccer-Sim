package com.keridano.soccersim.ui.main

data class MainViewUiState(
    val isSpinnerBlockingUi: Boolean = false,
    val isStartSimulationButtonActive: Boolean = true
) {
    fun spinnerVisible(visible: Boolean) = copy(isSpinnerBlockingUi = visible)
    fun startSimulationButtonActive(active: Boolean) = copy(isStartSimulationButtonActive = active)
}