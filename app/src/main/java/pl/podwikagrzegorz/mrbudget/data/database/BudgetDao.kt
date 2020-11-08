package pl.podwikagrzegorz.mrbudget.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction

@Dao
interface BudgetDao {

    @Insert
    suspend fun insertBudget(budget: DatabaseBudget)

    @Query("SELECT COUNT(*) from budget_table")
    suspend fun budgetsCount() : Int

    @Query("SELECT * FROM budget_table ORDER BY date DESC LIMIT 1")
    suspend fun getLatestBudget() : DatabaseBudget

    @Transaction
    @Query("SELECT * FROM budget_table WHERE budgetId = :key")
    suspend fun getAllExpenses(key: Long): BudgetWithExpenses?
}