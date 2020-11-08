package pl.podwikagrzegorz.mrbudget.data.repo

import pl.podwikagrzegorz.mrbudget.data.database.BudgetDao
import pl.podwikagrzegorz.mrbudget.data.database.asDomainModel
import pl.podwikagrzegorz.mrbudget.data.domain.Budget
import pl.podwikagrzegorz.mrbudget.data.domain.asDatabaseModel
import javax.inject.Inject

class BudgetRepository @Inject constructor(
    private val budgetDao: BudgetDao
) {

    suspend fun insertBudget(newBudget: Budget) =
        budgetDao.insertBudget(newBudget.asDatabaseModel())

    suspend fun getBudgetsCount() =
        budgetDao.budgetsCount()

    suspend fun getLatestBudget() =
        budgetDao.getLatestBudget().asDomainModel()
}