package pl.podwikagrzegorz.mrbudget.database.repo

import pl.podwikagrzegorz.mrbudget.data.database.BudgetWithExpenses
import pl.podwikagrzegorz.mrbudget.data.database.BudgetWithIncomes
import pl.podwikagrzegorz.mrbudget.data.database.DatabaseBudget
import pl.podwikagrzegorz.mrbudget.data.domain.*
import pl.podwikagrzegorz.mrbudget.data.repo.BudgetRepository
import java.util.*

class FakeBudgetRepository : BudgetRepository {
    private val _listOfExpenses = mutableListOf<Expense>()

    private val _listOfIncomes = mutableListOf<Income>()
    val listOfIncomes: List<Income>
        get() = _listOfIncomes

    private val _listOfBudgets = mutableListOf<Budget>()
    val listOfBudgets: List<Budget>
        get() = _listOfBudgets

    override suspend fun insertExpense(newExpense: Expense) {
        _listOfExpenses.add(newExpense)
    }

    override suspend fun insertIncome(newIncome: Income) {
        _listOfIncomes.add(newIncome)
    }

    override suspend fun insertBudget(newBudget: Budget) {
        _listOfBudgets.add(newBudget)
    }

    override suspend fun getBudgetsCount(): Int = _listOfBudgets.size

    override suspend fun getBudgetById(budgetId: Long): Budget =
        _listOfBudgets[budgetId.toInt()]


    override suspend fun getLatestBudget(): Budget = _listOfBudgets[_listOfBudgets.size - 1]

    override suspend fun getBudgetWithExpenses(budgetId: Long): BudgetWithExpenses {
        val searchBudget = _listOfBudgets.find { it.budgetId == budgetId }
            ?: return BudgetWithExpenses(DatabaseBudget(0, Date()), emptyList())

        return BudgetWithExpenses(searchBudget.asDatabaseModel(), emptyList())
    }

    override suspend fun getBudgetWithIncomes(budgetId: Long): BudgetWithIncomes {
        return BudgetWithIncomes(DatabaseBudget(0, Date()), emptyList())

    }

    override suspend fun getListOfExpenses(budgetId: Long): List<Expense> {
        return _listOfExpenses
    }

    override suspend fun deleteExpense(expenseToDelete: Expense) {
        _listOfExpenses.remove(expenseToDelete)
    }

    override suspend fun updateExpense(updatedExpense: Expense) {
        //
    }

    override suspend fun getAllBudgets(): List<BudgetWithExpensesAndIncomes> {
        return emptyList()
    }
}