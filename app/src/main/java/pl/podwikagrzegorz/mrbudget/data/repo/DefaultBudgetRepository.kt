package pl.podwikagrzegorz.mrbudget.data.repo

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import pl.podwikagrzegorz.mrbudget.data.database.*
import pl.podwikagrzegorz.mrbudget.data.domain.*
import javax.inject.Inject

class DefaultBudgetRepository @Inject constructor(
    private val budgetDao: BudgetDao
) : BudgetRepository {

    override suspend fun insertExpense(newExpense: Expense) =
        budgetDao.insertExpense(newExpense.asDatabaseModel())

    override suspend fun insertIncome(newIncome: Income) =
        budgetDao.insertIncome(newIncome.asDatabaseModel())

    override suspend fun insertBudget(newBudget: Budget) =
        budgetDao.insertBudget(newBudget.asDatabaseModel())

    override suspend fun getBudgetsCount() =
        budgetDao.budgetsCount()

    override suspend fun getLatestBudget() =
        budgetDao.getLatestBudget().asDomainModel()

    override suspend fun getBudgetWithExpenses(budgetId: Long) =
        budgetDao.getBudgetWithExpensesById(budgetId)

    override suspend fun getBudgetWithIncomes(budgetId: Long) =
        budgetDao.getAllIncomes(budgetId)

    override suspend fun getListOfExpenses(budgetId: Long) : List<Expense>{
        return withContext(Dispatchers.IO) {
            val budgetWithExpenses = getBudgetWithExpenses(budgetId)

            budgetWithExpenses.expenses.reverseAndConvertToDomainModel()
            //budgetWithExpenses.expenses.asDomainModel()
        }
    }

    override suspend fun deleteExpense(expenseToDelete: Expense) =
        budgetDao.deleteExpense(expenseToDelete.asDatabaseModel())

    override suspend fun updateExpense(updatedExpense: Expense) =
        budgetDao.updateExpense(updatedExpense.asDatabaseModel())

/*    suspend fun getAllBudgetsWithExpenses(): List<DomainBudgetWithExpenses> =
        budgetDao.getAllBudgetsWithExpenses().asDomainBudgetWithExpenses()

    suspend fun getALlBudgetsWithIncomes(): List<DomainBudgetWithIncomes> =
        budgetDao.getAllBudgetsWithIncomes().asDomainBudgetWithIncomes()*/

    override suspend fun getAllBudgets(): List<BudgetWithExpensesAndIncomes> {
        val budgetsWithExpenses =  budgetDao.getAllBudgetsWithExpenses()
        val budgetsWithIncomes = budgetDao.getAllBudgetsWithIncomes()
        return convertDbBudgetIntoDomainBudget(budgetsWithExpenses, budgetsWithIncomes)
    }

    private suspend fun convertDbBudgetIntoDomainBudget(budgetsWithExpenses: List<BudgetWithExpenses>, budgetsWithIncomes: List<BudgetWithIncomes>) : List<BudgetWithExpensesAndIncomes> {
        return withContext(Dispatchers.Default) {
            val listOfDomainBudgets = mutableListOf<BudgetWithExpensesAndIncomes>()

            for (i in budgetsWithExpenses.indices){
                listOfDomainBudgets.add(
                    BudgetWithExpensesAndIncomes(budget =  budgetsWithExpenses[i].budget.asDomainModel(),
                    expenses = budgetsWithExpenses[i].expenses.asListOfExpenses(),
                    incomes = budgetsWithIncomes[i].incomes.asListOfIncomes())
                )
            }

            listOfDomainBudgets
        }
    }
}