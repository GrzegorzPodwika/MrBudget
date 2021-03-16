package pl.podwikagrzegorz.mrbudget.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import pl.podwikagrzegorz.mrbudget.data.database.BudgetDao
import pl.podwikagrzegorz.mrbudget.data.database.BudgetDatabase
import pl.podwikagrzegorz.mrbudget.data.repo.BudgetRepository
import pl.podwikagrzegorz.mrbudget.data.repo.DefaultBudgetRepository
import pl.podwikagrzegorz.mrbudget.other.Constants
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
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

    @Singleton
    @Provides
    fun provideDefaultRepository(dao: BudgetDao): BudgetRepository = DefaultBudgetRepository(dao)
}