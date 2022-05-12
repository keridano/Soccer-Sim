package com.keridano.soccersim.ui.main

/**
 * Ui State object of the [MainFragment].
 * Used to change the widgets appearance
 */
data class MainViewUiState(
    val isSpinnerBlockingUi: Boolean = false,
    val isStartSimulationButtonActive: Boolean = true
) {
    fun spinnerVisible(visible: Boolean) = copy(isSpinnerBlockingUi = visible)
    fun startSimulationButtonActive(active: Boolean) = copy(isStartSimulationButtonActive = active)
}