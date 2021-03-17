package pl.podwikagrzegorz.mrbudget.ui.history

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.interpolator.view.animation.FastOutSlowInInterpolator
import com.google.android.material.color.MaterialColors
import com.google.android.material.transition.MaterialContainerTransform
import dagger.hilt.android.AndroidEntryPoint
import pl.podwikagrzegorz.mrbudget.R
import pl.podwikagrzegorz.mrbudget.databinding.CardviewPiechartExpensesBinding

@AndroidEntryPoint
class HistoryDetailsFragment : Fragment() {
    private lateinit var binding: CardviewPiechartExpensesBinding
    private val viewModel: HistoryDetailsViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = CardviewPiechartExpensesBinding.inflate(inflater, container, false)

        setupBinding()
        presetPieChartAppearance()
        observeData()

        return binding.root
    }

    private fun setupBinding() {
        binding.apply {
            lifecycleOwner = viewLifecycleOwner
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


    private fun observeData() {
        observeBudgetWithExpenses()
        observeTotalExpensesAndIncomes()
    }

    private fun observeBudgetWithExpenses() {
        viewModel.expensesPieData.observe(viewLifecycleOwner, { pieData ->
            binding.piechartExpenses.data = pieData
            binding.piechartExpenses.invalidate()
        })
    }

    private fun observeTotalExpensesAndIncomes() {
        viewModel.totalExpenses.observe(viewLifecycleOwner, { totalExpenses ->
            binding.totalExpenses = totalExpenses
        })

        viewModel.totalIncomes.observe(viewLifecycleOwner, { totalIncomes ->
            binding.totalIncomes = totalIncomes
        })
    }
}