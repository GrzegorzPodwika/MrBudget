package pl.podwikagrzegorz.mrbudget.data.repo

import pl.podwikagrzegorz.mrbudget.data.database.BudgetWithExpenses
import pl.podwikagrzegorz.mrbudget.data.database.BudgetWithIncomes
import pl.podwikagrzegorz.mrbudget.data.domain.Budget
import pl.podwikagrzegorz.mrbudget.data.domain.BudgetWithExpensesAndIncomes
import pl.podwikagrzegorz.mrbudget.data.domain.Expense
import pl.podwikagrzegorz.mrbudget.data.domain.Income

interface BudgetRepository {
    suspend fun insertExpense(newExpense: Expense)

    suspend fun insertIncome(newIncome: Income)

    suspend fun insertBudget(newBudget: Budget)

    suspend fun getBudgetsCount(): Int

    suspend fun getBudgetById(budgetId: Long) : Budget

    suspend fun getLatestBudget(): Budget

    suspend fun getBudgetWithExpenses(budgetId: Long): BudgetWithExpenses

    suspend fun getBudgetWithIncomes(budgetId: Long): BudgetWithIncomes

    suspend fun getListOfExpenses(budgetId: Long) : List<Expense>
    suspend fun deleteExpense(expenseToDelete: Expense)

    suspend fun updateExpense(updatedExpense: Expense)

    suspend fun getAllBudgets(): List<BudgetWithExpensesAndIncomes>
}