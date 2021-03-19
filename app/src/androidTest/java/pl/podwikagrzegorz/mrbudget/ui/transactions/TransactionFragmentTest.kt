package pl.podwikagrzegorz.mrbudget.ui.transactions

import android.os.Bundle
import androidx.core.os.bundleOf
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import androidx.navigation.Navigation
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.filters.MediumTest
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.hamcrest.core.IsNot
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito
import org.mockito.Mockito.verify
import pl.podwikagrzegorz.mrbudget.R
import pl.podwikagrzegorz.mrbudget.launchFragmentInHiltContainer
import pl.podwikagrzegorz.mrbudget.other.Constants

@HiltAndroidTest
@ExperimentalCoroutinesApi
@MediumTest
class TransactionFragmentTest {
    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Before
    fun setup() {
        hiltRule.inject()
    }

    @Test
    fun clickAddExpenseFAB_navigateToExpenseFragment() {
        // Given fresh activity scenario
        val navController = Mockito.mock(NavController::class.java)

        launchFragmentInHiltContainer<TransactionFragment>() {
            Navigation.setViewNavController(this.view!!, navController)


        }


        // When clicking on fab
        onView(withId(R.id.fabAddTransaction)).perform(click())

        // Then app should navigate to expense fragment
        verify(navController).navigate(
            R.id.navigation_expense,
        )

    }
}