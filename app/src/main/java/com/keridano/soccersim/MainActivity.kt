package com.keridano.soccersim

import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.keridano.soccersim.ui.main.MainFragment
import com.keridano.soccersim.ui.main.MainViewModel
import com.keridano.soccersim.ui.main.MainViewModelFactory

class MainActivity : BaseActivity<MainViewModel>() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, MainFragment.newInstance())
                .commitNow()
        }
    }

    override fun provideViewModel(): MainViewModel {
        return ViewModelProvider(
            this,
            MainViewModelFactory()
        )[MainViewModel::class.java]
    }
}