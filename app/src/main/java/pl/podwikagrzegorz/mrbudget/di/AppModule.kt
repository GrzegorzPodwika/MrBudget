package pl.podwikagrzegorz.mrbudget.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import pl.podwikagrzegorz.mrbudget.data.database.BudgetDatabase
import pl.podwikagrzegorz.mrbudget.other.Constants
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideBudgetDatabase(
        @ApplicationContext app: Context
    ) = Room.databaseBuilder(
        app,
        BudgetDatabase::class.java,
        Constants.DATABASE_NAME
    ).fallbackToDestructiveMigration()
        .build()

    @Singleton
    @Provides
    fun provideBudgetDao(db: BudgetDatabase) = db.budgetDao
}