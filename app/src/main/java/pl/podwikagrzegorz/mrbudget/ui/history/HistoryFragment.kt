package pl.podwikagrzegorz.mrbudget.ui.history

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import pl.podwikagrzegorz.mrbudget.databinding.HistoryFragmentBinding
import pl.podwikagrzegorz.mrbudget.ui.adapters.BudgetAdapter
import pl.podwikagrzegorz.mrbudget.ui.transactions.SharedViewModel

@AndroidEntryPoint
class HistoryFragment : Fragment() {

    private val viewModel: HistoryViewModel by viewModels()
    private lateinit var binding: HistoryFragmentBinding
    private val sharedViewModel : SharedViewModel by activityViewModels()
    private val budgetAdapter: BudgetAdapter = BudgetAdapter { budgetId ->
        // viewModel.onBudgetClick(budgetId)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = HistoryFragmentBinding.inflate(inflater, container, false)

        setUpBinding()
        observeListOfBudgets()
        observeIfTransactionHasBeenAdded()

        return binding.root
    }

    private fun setUpBinding() {
        binding.recViewHistoryBudgets.adapter = budgetAdapter
    }

    private fun observeListOfBudgets() {
        viewModel.budgetsWithExpensesAndIncomes.observe(viewLifecycleOwner, {
            budgetAdapter.submitList(it)
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