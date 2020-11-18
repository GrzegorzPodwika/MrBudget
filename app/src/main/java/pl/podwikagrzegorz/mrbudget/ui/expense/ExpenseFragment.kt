package pl.podwikagrzegorz.mrbudget.ui.expense

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.google.android.material.tabs.TabLayout
import dagger.hilt.android.AndroidEntryPoint
import pl.podwikagrzegorz.mrbudget.R
import pl.podwikagrzegorz.mrbudget.data.domain.ExpenseType
import pl.podwikagrzegorz.mrbudget.databinding.ExpenseFragmentBinding
import pl.podwikagrzegorz.mrbudget.ui.transactions.SharedViewModel

@AndroidEntryPoint
class ExpenseFragment : Fragment() {

    private lateinit var binding: ExpenseFragmentBinding
    private val viewModel: ExpenseViewModel by viewModels()
    private val sharedViewModel: SharedViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = ExpenseFragmentBinding.inflate(inflater, container, false)

        setupBindingWithViewModel()
        setOnTabSelectedListener()
        setOnClearButtonLongClickListener()
        setOnAddExpenseFABClickListener()
        observeInsertProcess()

        return binding.root
    }

    private fun setupBindingWithViewModel() {
        binding.apply {
            lifecycleOwner = viewLifecycleOwner
            expenseViewModel = viewModel
        }
    }

    private fun setOnTabSelectedListener() {
        binding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                val position = tab?.position
                position?.let {
                    if (position == 0) {
                        binding.radioGroupExpenses.visibility = View.VISIBLE
                    } else {
                        binding.radioGroupExpenses.visibility = View.INVISIBLE
                    }
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {}

            override fun onTabReselected(tab: TabLayout.Tab?) {}
        })
    }

    private fun setOnClearButtonLongClickListener() {
        binding.imageViewClear.setOnLongClickListener {
            viewModel.clearCompletelyMoney()
            true
        }
    }

    private fun setOnAddExpenseFABClickListener() {
        binding.fabAddTransaction.setOnClickListener {

            val selectedTabPosition = binding.tabLayout.selectedTabPosition

            if (selectedTabPosition == 0) {
                addExpense()
            } else {
                addIncome()
            }
        }
    }

    private fun addExpense() {
        val radioButtonId = binding.radioGroupExpenses.checkedRadioButtonId
        val radioButton = requireActivity().findViewById<RadioButton>(radioButtonId)

        val type: ExpenseType = when(radioButton.text) {
            getString(R.string.regular_expenses) -> ExpenseType.REGULAR
            getString(R.string.one_off_expenses) -> ExpenseType.ONE_OFF
            getString(R.string.savings) -> ExpenseType.SAVINGS
            getString(R.string.retirement) -> ExpenseType.RETIREMENT
            else -> ExpenseType.REGULAR
        }

        val addedSuccessfully = viewModel.addExpense(type)
        if (addedSuccessfully) {
            sharedViewModel.changeFlagToSuccess()
        }
    }

    private fun addIncome() {
        val addedSuccessfully = viewModel.addIncome()
        if (addedSuccessfully) {
            sharedViewModel.changeFlagToSuccess()
        }
    }

    private fun observeInsertProcess() {
        viewModel.insertSuccess.observe(viewLifecycleOwner, Observer { isSuccess ->
            if (isSuccess) {
                findNavController().popBackStack()
                viewModel.onInsertComplete()
            }
        })
    }
}
