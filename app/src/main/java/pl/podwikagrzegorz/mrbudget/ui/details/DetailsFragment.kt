package pl.podwikagrzegorz.mrbudget.ui.details

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.chip.Chip
import dagger.hilt.android.AndroidEntryPoint
import pl.podwikagrzegorz.mrbudget.R
import pl.podwikagrzegorz.mrbudget.data.domain.Expense
import pl.podwikagrzegorz.mrbudget.data.domain.ExpenseType
import pl.podwikagrzegorz.mrbudget.databinding.DetailsFragmentBinding
import pl.podwikagrzegorz.mrbudget.ui.adapters.EditDeleteListener
import pl.podwikagrzegorz.mrbudget.ui.adapters.ExpenseAdapter
import pl.podwikagrzegorz.mrbudget.ui.transactions.SharedViewModel

@AndroidEntryPoint
class DetailsFragment : Fragment() {

    private lateinit var binding: DetailsFragmentBinding
    private val viewModel: DetailsViewModel by viewModels()
    private val sharedViewModel : SharedViewModel by activityViewModels()
    private val expenseAdapter = ExpenseAdapter(object : EditDeleteListener {
        override fun onEditClick(expense: Expense) {
            showEditExpenseDialog(expense)
        }

        override fun onDeleteClick(expense: Expense) {
            viewModel.deleteExpense(expense)
        }
    })

    private fun showEditExpenseDialog(expenseToEdit: Expense) {
        EditExpenseDialog(expenseToEdit) {updatedExpense ->
            viewModel.updateExpense(updatedExpense)
        }.show(childFragmentManager, null)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DetailsFragmentBinding.inflate(inflater, container, false)

        setUpBindingView()
        observeListOfExpenses()
        observeIfTransactionHasBeenAdded()
        setUpChipGroupExpenseType()

        return binding.root
    }

    private fun setUpBindingView() {
        binding.apply {
            lifecycleOwner = viewLifecycleOwner
            recViewExpenses.addItemDecoration(DividerItemDecoration(recViewExpenses.context, (recViewExpenses.layoutManager!! as LinearLayoutManager).orientation))
            recViewExpenses.adapter = expenseAdapter
        }
    }


    private fun observeListOfExpenses() {
        viewModel.listOfExpenses.observe(viewLifecycleOwner, {
            expenseAdapter.submitListOfExpenses(it)
            val expenseType = getCurrentExpenseTypeFromChipGroup()
            expenseAdapter.filterList(expenseType)
        })
    }

    private fun getCurrentExpenseTypeFromChipGroup(): ExpenseType? {
        val checkedId =  binding.chipGroup.checkedChipId
        return if (checkedId == View.NO_ID) {
            null
        } else {
            when(binding.chipGroup.findViewById<Chip>(checkedId)!!.text) {
                getString(R.string.groceries) -> ExpenseType.GROCERIES
                getString(R.string.transport) -> ExpenseType.TRANSPORT
                getString(R.string.health) -> ExpenseType.HEALTH
                getString(R.string.family) -> ExpenseType.FAMILY
                getString(R.string.gifts) -> ExpenseType.GIFTS
                getString(R.string.education) -> ExpenseType.EDUCATION
                getString(R.string.home) -> ExpenseType.HOME
                getString(R.string.hobby) -> ExpenseType.HOBBY
                else -> null
            }
        }
    }

    private fun observeIfTransactionHasBeenAdded() {
        sharedViewModel.isAddedExpenseOrIncome.observe(viewLifecycleOwner, { isAdded ->
            if (isAdded) {
                viewModel.fetchFreshData()
                sharedViewModel.refreshBarChartComplete()
            }
        })
    }

    private fun setUpChipGroupExpenseType() {
        binding.chipGroup.setOnCheckedChangeListener { group, checkedId ->
            val titleOrNull = group.findViewById<Chip>(checkedId)?.text
            if (titleOrNull != null) {
                when (titleOrNull) {
                    getString(R.string.groceries) -> expenseAdapter.filterList(ExpenseType.GROCERIES)
                    getString(R.string.transport) -> expenseAdapter.filterList(ExpenseType.TRANSPORT)
                    getString(R.string.health) -> expenseAdapter.filterList(ExpenseType.HEALTH)
                    getString(R.string.family) -> expenseAdapter.filterList(ExpenseType.FAMILY)
                    getString(R.string.gifts) -> expenseAdapter.filterList(ExpenseType.GIFTS)
                    getString(R.string.education) -> expenseAdapter.filterList(ExpenseType.EDUCATION)
                    getString(R.string.home) -> expenseAdapter.filterList(ExpenseType.HOME)
                    getString(R.string.hobby) -> expenseAdapter.filterList(ExpenseType.HOBBY)

                    else -> expenseAdapter.filterList(null)
                }
            } else {
                expenseAdapter.filterList(null)
            }
        }
    }


}