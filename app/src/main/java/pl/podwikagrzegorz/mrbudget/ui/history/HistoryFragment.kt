package pl.podwikagrzegorz.mrbudget.ui.history

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import dagger.hilt.android.AndroidEntryPoint
import pl.podwikagrzegorz.mrbudget.R
import pl.podwikagrzegorz.mrbudget.data.domain.BudgetWithExpensesAndIncomes
import pl.podwikagrzegorz.mrbudget.databinding.HistoryFragmentBinding
import pl.podwikagrzegorz.mrbudget.ui.adapters.BudgetAdapter
import pl.podwikagrzegorz.mrbudget.ui.adapters.OnBudgetClickListener
import java.util.*

@AndroidEntryPoint
class HistoryFragment : Fragment() {

    private val viewModel: HistoryViewModel by viewModels()
    private lateinit var binding: HistoryFragmentBinding
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

}