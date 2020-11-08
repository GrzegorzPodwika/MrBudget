package pl.podwikagrzegorz.mrbudget

import android.app.Application
import android.content.res.Resources
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

@HiltAndroidApp
class BudgetApp : Application(){

    override fun onCreate() {
        super.onCreate()

        Timber.plant(Timber.DebugTree())
        res = resources
    }

    companion object {
        lateinit var res: Resources
    }
}