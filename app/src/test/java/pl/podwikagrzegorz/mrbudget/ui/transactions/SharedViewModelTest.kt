package pl.podwikagrzegorz.mrbudget.ui.transactions

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.`is`
import org.junit.Rule
import org.junit.Test

class SharedViewModelTest {

    /*A JUnit Test Rule that swaps the background executor used by the Architecture Components
    * with a different one which executes each task synchronously.
    * When you write tests that include testing LiveData, use this rule!
     */
    @get:Rule
    var instantExecutionRule = InstantTaskExecutorRule()

    @Test
    fun `change flag to true sets it correctly`() {
        // Given a fresh viewmodel
        val sharedViewModel = SharedViewModel()

        // When setting a flag
        sharedViewModel.changeFlagToSuccess()

        // Then the flag is set correctly
        val flag = sharedViewModel.isAddedExpenseOrIncome.getOrAwaitValue()

        assertThat(flag, `is`(true))
    }

    @Test
    fun `change flag to false sets it correctly`() {
        // Given a fresh viewmodel
        val sharedViewModel = SharedViewModel()

        // When setting a flag
        sharedViewModel.refreshBarChartComplete()

        // Then the flag is set correctly
        val flag = sharedViewModel.isAddedExpenseOrIncome.getOrAwaitValue()

        assertThat(flag, `is`(false))
    }

}