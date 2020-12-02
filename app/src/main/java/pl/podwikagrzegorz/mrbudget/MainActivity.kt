package pl.podwikagrzegorz.mrbudget

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.navigation.NavOptions
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import pl.podwikagrzegorz.mrbudget.data.domain.Budget
import pl.podwikagrzegorz.mrbudget.data.repo.BudgetRepository
import pl.podwikagrzegorz.mrbudget.databinding.ActivityMainBinding
import pl.podwikagrzegorz.mrbudget.other.Constants
import java.util.*
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val navController by lazy { findNavController(R.id.nav_host_fragment) }
    @Inject lateinit var repository: BudgetRepository
    private var budgetId: Long = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupNavigation()
        setFabListener()
        checkIfNewBudgetShouldBeAdded()
        observePreferenceRepository()
    }

    private fun setupNavigation() {
        binding.navView.setupWithNavController(navController)
        navController.addOnDestinationChangedListener { _, destination, _ ->
            if (destination.id == R.id.navigation_expense) {
                binding.bottomAppBar.visibility = View.GONE
                binding.fabAddTransaction.visibility = View.GONE
            } else {
                binding.bottomAppBar.visibility = View.VISIBLE
                binding.fabAddTransaction.visibility = View.VISIBLE
            }
        }

        binding.navView.background = null
        binding.navView.menu.getItem(2).isEnabled = false
    }

    private fun setFabListener() {
        binding.fabAddTransaction.setOnClickListener {
            val navOptions = NavOptions.Builder()
                .setEnterAnim(R.anim.bottom_sheet_slide_in)
                .setExitAnim(R.anim.bottom_sheet_slide_out)
                .setPopEnterAnim(android.R.anim.fade_in)
                .setPopExitAnim(R.anim.bottom_sheet_slide_out)
                .build()

            val bundle = bundleOf(
                Constants.BUDGET_ID to budgetId
            )

            navController.navigate(R.id.navigation_expense, bundle, navOptions)
        }
    }

    private fun checkIfNewBudgetShouldBeAdded() {
        CoroutineScope(Dispatchers.IO).launch {
            val budgetsCount = repository.getBudgetsCount()

            if (budgetsCount == 0) {
                repository.insertBudget(Budget(0, Date()))
            }

            val lastBudget = repository.getLatestBudget()

            if (currentDateIsOneMonthAfterLastBudget(Date(), lastBudget.date)) {
                repository.insertBudget(Budget(0, Date()))
            }

            budgetId = repository.getLatestBudget().budgetId
        }
    }

    private fun currentDateIsOneMonthAfterLastBudget(now: Date, lastBudgetDate: Date): Boolean {
        val calendar = Calendar.getInstance()

        calendar.time = now
        val nowMonth = calendar.get(Calendar.MONTH)

        calendar.time = lastBudgetDate
        val lastBudgetMonth = calendar.get(Calendar.MONTH)

        return nowMonth != lastBudgetMonth
    }

    private fun observePreferenceRepository() {
        (application as BudgetApp).preferenceRepository.nightModeLive.observe(this, { nightMode ->
            nightMode?.let { delegate.localNightMode = it }
        })
    }

}