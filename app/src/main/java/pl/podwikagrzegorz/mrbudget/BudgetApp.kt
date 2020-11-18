package pl.podwikagrzegorz.mrbudget

import android.app.Application
import android.content.res.Resources
import dagger.hilt.android.HiltAndroidApp
import pl.podwikagrzegorz.mrbudget.data.repo.PreferenceRepository
import timber.log.Timber

@HiltAndroidApp
class BudgetApp : Application(){
    lateinit var preferenceRepository: PreferenceRepository

    override fun onCreate() {
        super.onCreate()

        Timber.plant(Timber.DebugTree())
        res = resources

        preferenceRepository = PreferenceRepository(getSharedPreferences(DEFAULT_PREFERENCES, MODE_PRIVATE))
    }

    companion object {
        lateinit var res: Resources
        const val DEFAULT_PREFERENCES = "default_preferences"
    }
}