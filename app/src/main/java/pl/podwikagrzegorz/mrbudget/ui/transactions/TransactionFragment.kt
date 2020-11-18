package pl.podwikagrzegorz.mrbudget.ui.transactions

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.github.mikephil.charting.components.Description
import dagger.hilt.android.AndroidEntryPoint
import pl.podwikagrzegorz.mrbudget.R
import pl.podwikagrzegorz.mrbudget.databinding.TransactionFragmentBinding
import timber.log.Timber

@AndroidEntryPoint
class TransactionFragment : Fragment() {

    private lateinit var binding: TransactionFragmentBinding
    private val viewModel: TransactionViewModel by viewModels()
    private val sharedViewModel : SharedViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = TransactionFragmentBinding.inflate(inflater, container, false)

        setUpBindingWithViewModel()
        presetPieChartAppearance()
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