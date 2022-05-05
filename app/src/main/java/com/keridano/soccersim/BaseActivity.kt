package com.keridano.soccersim

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import com.keridano.soccersim.extension.TAG
import com.keridano.soccersim.util.Logger

/**
 * Base class for all activities
 */
abstract class BaseActivity<VM> : AppCompatActivity() where VM : ViewModel {

    protected val logger = Logger(TAG)

    lateinit var viewModel: VM

    abstract fun provideViewModel(): VM

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = provideViewModel()
    }

}