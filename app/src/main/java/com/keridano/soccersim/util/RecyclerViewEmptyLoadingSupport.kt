package com.keridano.soccersim.util

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.AdapterDataObserver
import com.keridano.soccersim.extension.visible

/**
 * This class extends [RecyclerView] in order to add empty and loading views feature
 * It is based on a [AdapterDataObserver] which observe the changes on the [RecyclerView] items
 * And switches to the correct view accordingly
 */
class RecyclerViewEmptyLoadingSupport : RecyclerView {

    var stateView: RecyclerViewUiState? = RecyclerViewUiState.LOADING
        set(value) {
            field = value
            setState()
        }
    var emptyStateView: View? = null
    var loadingStateView: View? = null

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(
        context,
        attrs,
        defStyle
    )

    private val dataObserver = object : AdapterDataObserver() {
        override fun onChanged() {
            onChangeState()
        }

        override fun onItemRangeRemoved(positionStart: Int, itemCount: Int) {
            super.onItemRangeRemoved(positionStart, itemCount)
            onChangeState()
        }

        override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
            super.onItemRangeInserted(positionStart, itemCount)
            onChangeState()
        }
    }

    override fun setAdapter(adapter: Adapter<*>?) {
        super.setAdapter(adapter)
        adapter?.registerAdapterDataObserver(dataObserver)
        dataObserver.onChanged()
    }

    fun onChangeState() {
        if (adapter?.itemCount == 0) {
            emptyStateView?.visible(true)
            loadingStateView?.visible(false)
            this.visible(false)
        } else {
            emptyStateView?.visible(false)
            loadingStateView?.visible(false)
            this.visible(true)
        }
    }

    private fun setState() {
        when (this.stateView) {
            RecyclerViewUiState.LOADING -> {
                loadingStateView?.visible(true)
                this.visible(false)
                emptyStateView?.visible(false)
            }
            RecyclerViewUiState.NORMAL -> {
                loadingStateView?.visible(false)
                this.visible(true)
                emptyStateView?.visible(false)
            }
            RecyclerViewUiState.EMPTY -> {
                loadingStateView?.visible(false)
                this.visible(false)
                emptyStateView?.visible(true)
            }
            else -> {
                // should never enter here.
                loadingStateView?.visible(false)
                this.visible(false)
                emptyStateView?.visible(true)
            }
        }
    }
}