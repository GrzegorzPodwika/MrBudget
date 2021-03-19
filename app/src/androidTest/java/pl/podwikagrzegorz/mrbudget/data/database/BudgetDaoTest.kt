package pl.podwikagrzegorz.mrbudget.data.database

import android.util.Log
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.matcher.ViewMatchers.assertThat
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.Matchers.*
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import pl.podwikagrzegorz.mrbudget.data.domain.ExpenseType
import pl.podwikagrzegorz.mrbudget.getOrAwaitValue
import java.util.*

@RunWith(AndroidJUnit4::class)
@SmallTest
@ExperimentalCoroutinesApi
class BudgetDaoTest {
    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var database: BudgetDatabase
    private lateinit var dao: BudgetDao
    private val EXPENSE_NAME = "EXPENSE_NAME"
    private val expenseType = ExpenseType.HOBBY
    private val AMOUNT = 10.0
    private val BUDGET_OWNER_ID = 0L


    @Before
    fun setup() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            BudgetDatabase::class.java
        ).allowMainThreadQueries().build()

        dao = database.budgetDao
    }

    @After
    fun cleanup() {
        database.close()
    }

    @Test
    fun insertExpense() = runBlockingTest {
        // Given fresh budget and expense
        val databaseBudget = DatabaseBudget(BUDGET_OWNER_ID, Date())
        val databaseExpense = DatabaseExpense((BUDGET_OWNER_ID + 1), BUDGET_OWNER_ID, EXPENSE_NAME, expenseType.name, AMOUNT)

        // When inserting budget with associated expense
        dao.insertBudget(databaseBudget)
        dao.insertExpense(databaseExpense)

        val expenses = dao.observeExpensesByBudgetId(BUDGET_OWNER_ID).getOrAwaitValue()

        // Then check if expense has been added successfully
        assertThat(expenses, contains(databaseExpense))
    }

    @Test
    fun insertIncome() = runBlockingTest {
        // Given fresh budget and expense
        val budget = DatabaseBudget(BUDGET_OWNER_ID, Date())
        val income = DatabaseIncome((BUDGET_OWNER_ID + 1), BUDGET_OWNER_ID, EXPENSE_NAME, AMOUNT)

        // When inserting budget with associated income
        dao.insertBudget(budget)
        dao.insertIncome(income)

        val incomes = dao.observeIncomesByBudgetId(BUDGET_OWNER_ID).getOrAwaitValue()

        // Then check if income has been added successfully
        assertThat(incomes, contains(income))
    }

    @Test
    fun countNumberOfBudgets() = runBlockingTest {
        // Given
        val budget = DatabaseBudget(BUDGET_OWNER_ID, Date())
        val budget2 = DatabaseBudget(BUDGET_OWNER_ID, Date())

        // When
        dao.insertBudget(budget)
        dao.insertBudget(budget2)

        val noBudgets = dao.budgetsCount()

        // Then
        assertThat(noBudgets, `is`(2))
    }

    @Test
    fun fetchBudgetById() = runBlockingTest {
        // Given
        val budget = DatabaseBudget(1, Date())

        val noBudgets = dao.budgetsCount()
        Log.i(this@BudgetDaoTest.javaClass.name, "noBudgets = $noBudgets")

        // When
        dao.insertBudget(budget)

        val fetchedBudget = dao.getLatestBudget()
        val assumedNoBudget = dao.getBudgetById(100)

        // Then
        assertThat(fetchedBudget, `is`(budget))
        assertThat(assumedNoBudget, nullValue())
    }

    @Test
    fun deleteExpense() = runBlockingTest {
        // Given
        val budget = DatabaseBudget(BUDGET_OWNER_ID, Date())
        val expense = DatabaseExpense((BUDGET_OWNER_ID + 1), BUDGET_OWNER_ID, EXPENSE_NAME, expenseType.name, AMOUNT)

        // When inserting budget with associated expense and deleting expense after it
        dao.insertBudget(budget)
        dao.insertExpense(expense)
        dao.deleteExpense(expense)

        val expenses = dao.observeExpensesByBudgetId(BUDGET_OWNER_ID).getOrAwaitValue()

        // Then check if expense has been deleted successfully
        assertThat(expenses, `is`(emptyList()))
    }

    @Test
    fun updateExpense() = runBlockingTest {
        // Given
        val budget = DatabaseBudget(BUDGET_OWNER_ID, Date())
        val expense = DatabaseExpense((BUDGET_OWNER_ID + 1), BUDGET_OWNER_ID, EXPENSE_NAME, expenseType.name, AMOUNT)

        // When inserting budget with associated expense and updating expense after it
        dao.insertBudget(budget)
        dao.insertExpense(expense)

        val updatedExpense = DatabaseExpense((BUDGET_OWNER_ID + 1), BUDGET_OWNER_ID, EXPENSE_NAME, expenseType.name, 100.0)
        dao.updateExpense(updatedExpense)

        val expenses = dao.observeExpensesByBudgetId(BUDGET_OWNER_ID).getOrAwaitValue()

        // Then check if expense has been deleted successfully
        assertThat(expenses, contains(updatedExpense))
    }

}