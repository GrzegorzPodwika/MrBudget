package pl.podwikagrzegorz.mrbudget.ui.history

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.google.android.material.transition.MaterialFadeThrough
import dagger.hilt.android.AndroidEntryPoint
import pl.podwikagrzegorz.mrbudget.R
import pl.podwikagrzegorz.mrbudget.databinding.HistoryFragmentBinding
import pl.podwikagrzegorz.mrbudget.other.Constants
import pl.podwikagrzegorz.mrbudget.ui.adapters.BudgetAdapter
import pl.podwikagrzegorz.mrbudget.ui.transactions.SharedViewModel

@AndroidEntryPoint
class HistoryFragment : Fragment() {

    private val viewModel: HistoryViewModel by viewModels()
    private lateinit var binding: HistoryFragmentBinding
    private val sharedViewModel: SharedViewModel by activityViewModels()
    private val budgetAdapter: BudgetAdapter = BudgetAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enterTransition = MaterialFadeThrough()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = HistoryFragmentBinding.inflate(inflater, container, false)

        setUpBinding()
        setUpAdapter()
        observeListOfBudgets()
        observeIfTransactionHasBeenAdded()

        return binding.root
    }

    private fun setUpBinding() {
        binding.recViewHistoryBudgets.adapter = budgetAdapter
    }

    private fun setUpAdapter() {
        budgetAdapter.listener = BudgetAdapter.OnBudgetClickListener { budgetId ->
            val navOptions = NavOptions.Builder()
                .setEnterAnim(R.anim.fragment_grow_from_center)
                .setExitAnim(R.anim.fragment_hide_to_center)
                .setPopEnterAnim(android.R.anim.fade_in)
                .setPopExitAnim(R.anim.fragment_hide_to_center)
                .build()

            val bundle = bundleOf(
                Constants.BUDGET_ID to budgetId
            )

            findNavController().navigate(R.id.navigation_history_details, bundle, navOptions)
        }
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