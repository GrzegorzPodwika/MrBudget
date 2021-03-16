package pl.podwikagrzegorz.mrbudget.ui.transactions

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.github.mikephil.charting.components.Description
import dagger.hilt.android.AndroidEntryPoint
import pl.podwikagrzegorz.mrbudget.R
import pl.podwikagrzegorz.mrbudget.databinding.TransactionFragmentBinding
import pl.podwikagrzegorz.mrbudget.other.Constants
import timber.log.Timber

@AndroidEntryPoint
class TransactionFragment : Fragment() {

    private lateinit var binding: TransactionFragmentBinding
    private val viewModel: TransactionViewModel by viewModels()
    private val sharedViewModel : SharedViewModel by activityViewModels()
    private val navController by lazy { findNavController() }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = TransactionFragmentBinding.inflate(inflater, container, false)

        setUpBindingWithViewModel()
        presetPieChartAppearance()
        setupFabListener()
        observeBudgetWithExpenses()
        observeIfTransactionHasBeenAdded()

        return binding.root
    }

    private fun setUpBindingWithViewModel() {
        binding.apply {
            lifecycleOwner = viewLifecycleOwner
            transactionViewModel = viewModel
        }
    }

    private fun presetPieChartAppearance() {
        binding.piechartExpenses.apply {
            setUsePercentValues(true)
            centerText = getString(R.string.expenses)
            setCenterTextSize(20f)
            description.isEnabled = false
            legend.isEnabled = false
        }
    }

    private fun setupFabListener() {
        binding.fabAddTransaction.setOnClickListener {
            val navOptions = NavOptions.Builder()
                .setEnterAnim(R.anim.bottom_sheet_slide_in)
                .setExitAnim(R.anim.bottom_sheet_slide_out)
                .setPopEnterAnim(android.R.anim.fade_in)
                .setPopExitAnim(R.anim.bottom_sheet_slide_out)
                .build()

            val bundle = bundleOf(
                Constants.BUDGET_ID to viewModel.latestBudget.value!!.budgetId
            )

            navController.navigate(R.id.navigation_expense, bundle, navOptions)
        }
    }

    private fun observeBudgetWithExpenses() {
        viewModel.expensesPieData.observe(viewLifecycleOwner, { pieData ->
            binding.piechartExpenses.data = pieData
            binding.piechartExpenses.invalidate()
        })
    }

    private fun observeIfTransactionHasBeenAdded() {
        sharedViewModel.isAddedExpenseOrIncome.observe(viewLifecycleOwner, {isAdded ->
            if (isAdded) {
                viewModel.fetchFreshData()
                sharedViewModel.refreshBarChartComplete()
            }
        })
    }
}