package com.keridano.soccersim.util

/**
 * Enum used to check the Ui states if recycler views
 */
enum class RecyclerViewUiState {
    /**
     * Business logic in action
     */
    LOADING,

    /**
     * Recycler view is completely loaded
     */
    NORMAL,

    /**
     * Recycler view does not have any items
     */
    EMPTY
}