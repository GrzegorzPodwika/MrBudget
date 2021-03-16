package pl.podwikagrzegorz.mrbudget.ui.transactions

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.*
import org.junit.Rule
import org.junit.Test
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.runner.RunWith
import pl.podwikagrzegorz.mrbudget.data.domain.Budget
import pl.podwikagrzegorz.mrbudget.other.isTheSameMonth
import java.util.*

@ExperimentalCoroutinesApi
class TransactionViewModelTest {
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()
    private lateinit var fakeBudgetRepository: FakeBudgetRepository

    @Before
    fun init() {
        fakeBudgetRepository = FakeBudgetRepository()
    }

    @Test
    fun `checks if viewmodel inserts first budget with no budgets in db`() {
        // Given a fresh ViewModel
        val transactionViewModel = TransactionViewModel(fakeBudgetRepository)

        // Then
        val latestBudget = transactionViewModel.latestBudget.getOrAwaitValue()

        assertThat(latestBudget, not(nullValue()))
    }

    @Test
    fun `checks if viewmodel inserts next budget with new month case`() {
        // given
        setupDBWithOldBudget()
        val transactionViewModel = TransactionViewModel(fakeBudgetRepository)
        val now = Date()

        // when
        var noBudgets = 0
        var latestBudget: Budget? = null
        runBlockingTest {
            noBudgets = fakeBudgetRepository.getBudgetsCount()
            latestBudget = fakeBudgetRepository.getLatestBudget()
        }

        // then
        assertEquals("", noBudgets, 2)
        assertTrue(latestBudget!!.date.isTheSameMonth(now))

    }

    private fun setupDBWithOldBudget() {
        val calendar = GregorianCalendar()

        calendar.add(Calendar.MONTH, -5);
        val budgetNow = Budget(0, calendar.time)

        runBlockingTest {
            fakeBudgetRepository.insertBudget(budgetNow)
        }
    }

    @Test
    fun `checks if viewmodel fetching latest budget is correct`() {
        // Given a fresh ViewModel
        val currentBudget = Budget(0, Date())
        runBlockingTest {
            fakeBudgetRepository.insertBudget(currentBudget)
        }
        val transactionViewModel = TransactionViewModel(fakeBudgetRepository)

        // Then
        val latestBudget = transactionViewModel.latestBudget.getOrAwaitValue()

        assertThat(latestBudget, `is`(currentBudget))
    }

    @Test
    fun `checks if viewmodel fetching data is correct`() {
        // Given a fresh ViewModel
        val currentBudget = Budget(0, Date())
        runBlockingTest {
            fakeBudgetRepository.insertBudget(currentBudget)
        }
        val transactionViewModel = TransactionViewModel(fakeBudgetRepository)

        // Then
        val latestBudget = transactionViewModel.latestBudget.getOrAwaitValue()

        assertThat(latestBudget, `is`(currentBudget))
    }
}