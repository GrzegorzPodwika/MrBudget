package pl.podwikagrzegorz.mrbudget.ui.transactions

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityOptionsCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.interpolator.view.animation.FastOutSlowInInterpolator
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import androidx.transition.TransitionManager
import com.google.android.material.color.MaterialColors
import com.google.android.material.transition.MaterialArcMotion
import com.google.android.material.transition.MaterialContainerTransform
import com.google.android.material.transition.MaterialFadeThrough
import dagger.hilt.android.AndroidEntryPoint
import pl.podwikagrzegorz.mrbudget.R
import pl.podwikagrzegorz.mrbudget.databinding.TransactionFragmentBinding
import pl.podwikagrzegorz.mrbudget.other.Constants

@AndroidEntryPoint
class TransactionFragment : Fragment() {

    private lateinit var binding: TransactionFragmentBinding
    private val viewModel: TransactionViewModel by viewModels()
    private val sharedViewModel: SharedViewModel by activityViewModels()
    private val navController by lazy { findNavController() }
    private val navOptions = NavOptions.Builder()
        .setEnterAnim(R.anim.fragment_grow_from_lower_right)
        .setExitAnim(R.anim.fragment_hide_to_lower_right)
        .setPopEnterAnim(android.R.anim.fade_in)
        .setPopExitAnim(R.anim.fragment_hide_to_lower_right)
        .build()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enterTransition = MaterialFadeThrough()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = TransactionFragmentBinding.inflate(inflater, container, false)

        setUpBindingWithViewModel()
        presetPieChartAppearance()
        setupFabListener()
        observeData()

        return binding.root
    }

    private fun setUpBindingWithViewModel() {
        binding.apply {
            lifecycleOwner = viewLifecycleOwner
            transactionViewModel = viewModel
        }
    }

    private fun presetPieChartAppearance() {
        binding.cardviewExpenses.piechartExpenses.apply {
            setUsePercentValues(true)
            centerText = getString(R.string.expenses)
            setCenterTextSize(20f)
            description.isEnabled = false
            legend.isEnabled = false
        }
    }

    private fun setupFabListener() {
        binding.fabAddTransaction.setOnClickListener {

            val bundle = bundleOf(
                Constants.BUDGET_ID to viewModel.latestBudget.value!!.budgetId
            )

            navController.navigate(R.id.navigation_expense, bundle, navOptions)
        }
    }


    private fun observeData() {
        observeBudgetWithExpenses()
        observeTotalExpensesAndIncomes()
        observeIfTransactionHasBeenAdded()
    }

    private fun observeBudgetWithExpenses() {
        viewModel.expensesPieData.observe(viewLifecycleOwner, { pieData ->
            binding.cardviewExpenses.piechartExpenses.data = pieData
            binding.cardviewExpenses.piechartExpenses.invalidate()
        })
    }

    private fun observeTotalExpensesAndIncomes() {
        viewModel.totalExpenses.observe(viewLifecycleOwner, { totalExpenses ->
            binding.cardviewExpenses.totalExpenses = totalExpenses
        })

        viewModel.totalIncomes.observe(viewLifecycleOwner, { totalIncomes ->
            binding.cardviewExpenses.totalIncomes = totalIncomes
        })
    }

    private fun observeIfTransactionHasBeenAdded() {
        sharedViewModel.isAddedExpenseOrIncome.observe(viewLifecycleOwner, { isAdded ->
            if (isAdded) {
                viewModel.fetchFreshData()
                sharedViewModel.refreshBarChartComplete()
            }
        })
    }
}