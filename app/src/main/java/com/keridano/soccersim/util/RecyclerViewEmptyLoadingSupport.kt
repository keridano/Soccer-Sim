package com.keridano.soccersim.util

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.AdapterDataObserver

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
            emptyStateView?.visibility = View.VISIBLE
            loadingStateView?.visibility = View.GONE
            visibility = View.GONE
        } else {
            emptyStateView?.visibility = View.GONE
            loadingStateView?.visibility = View.GONE
            visibility = View.VISIBLE
        }
    }

    private fun setState() {
        when (this.stateView) {
            RecyclerViewUiState.LOADING -> {
                loadingStateView?.visibility = View.VISIBLE
                visibility = View.GONE
                emptyStateView?.visibility = View.GONE
            }
            RecyclerViewUiState.NORMAL -> {
                loadingStateView?.visibility = View.GONE
                visibility = View.VISIBLE
                emptyStateView?.visibility = View.GONE
            }
            RecyclerViewUiState.EMPTY -> {
                loadingStateView?.visibility = View.GONE
                visibility = View.GONE
                emptyStateView?.visibility = View.VISIBLE
            }
            else -> {
                // should never enter here.
                loadingStateView?.visibility = View.GONE
                visibility = View.GONE
                emptyStateView?.visibility = View.VISIBLE
            }
        }
    }
}