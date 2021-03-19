package pl.podwikagrzegorz.mrbudget.data.database

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface BudgetDao {

    @Insert
    suspend fun insertExpense(expense: DatabaseExpense)

    @Insert
    suspend fun insertIncome(income: DatabaseIncome)

    @Insert
    suspend fun insertBudget(budget: DatabaseBudget)

    @Query("SELECT COUNT(*) from budget_table")
    suspend fun budgetsCount(): Int

    @Query("SELECT * FROM budget_table WHERE budgetId = :key")
    suspend fun getBudgetById(key: Long): DatabaseBudget

    @Query("SELECT * FROM budget_table ORDER BY date DESC LIMIT 1")
    suspend fun getLatestBudget(): DatabaseBudget

    @Transaction
    @Query("SELECT * FROM budget_table WHERE budgetId = :key")
    suspend fun getBudgetWithExpensesById(key: Long): BudgetWithExpenses

    @Query("SELECT * FROM expense_table WHERE budgetOwnerId = :key")
    fun observeExpensesByBudgetId(key: Long): LiveData<List<DatabaseExpense>>

    @Query("SELECT * FROM income_table WHERE budgetOwnerId = :key")
    fun observeIncomesByBudgetId(key: Long): LiveData<List<DatabaseIncome>>


    @Transaction
    @Query("SELECT * FROM budget_table")
    suspend fun getAllBudgetsWithExpenses(): List<BudgetWithExpenses>

    @Transaction
    @Query("SELECT * FROM budget_table")
    suspend fun getAllBudgetsWithIncomes(): List<BudgetWithIncomes>

    @Transaction
    @Query("SELECT * FROM budget_table WHERE budgetId = :key")
    suspend fun getAllIncomes(key: Long): BudgetWithIncomes

    @Delete
    suspend fun deleteExpense(expense: DatabaseExpense)

    @Update
    suspend fun updateExpense(updatedExpense: DatabaseExpense)
}