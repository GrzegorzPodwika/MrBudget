package pl.podwikagrzegorz.mrbudget.ui.expense

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.interpolator.view.animation.FastOutSlowInInterpolator
import androidx.navigation.fragment.findNavController
import com.google.android.material.color.MaterialColors
import com.google.android.material.tabs.TabLayout
import com.google.android.material.transition.MaterialArcMotion
import com.google.android.material.transition.MaterialContainerTransform
import dagger.hilt.android.AndroidEntryPoint
import pl.podwikagrzegorz.mrbudget.R
import pl.podwikagrzegorz.mrbudget.data.domain.ExpenseType
import pl.podwikagrzegorz.mrbudget.databinding.ExpenseFragmentBinding
import pl.podwikagrzegorz.mrbudget.other.resIdByName
import pl.podwikagrzegorz.mrbudget.ui.adapters.ExpenseTypeAdapter
import pl.podwikagrzegorz.mrbudget.ui.transactions.SharedViewModel
import java.util.*

@AndroidEntryPoint
class ExpenseFragment : Fragment() {

    private lateinit var binding: ExpenseFragmentBinding
    private val viewModel: ExpenseViewModel by viewModels()
    private val sharedViewModel: SharedViewModel by activityViewModels()
    private lateinit var adapter: ExpenseTypeAdapter

    private var selectedExpenseType: ExpenseType? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = ExpenseFragmentBinding.inflate(inflater, container, false)

        setupBindingWithViewModel()
        setupExpenseTypeAdapter()
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

    private fun setupExpenseTypeAdapter() {
        val expenseTypeIcons = mutableListOf<Drawable>()

        ExpenseType.values().forEach { type ->
            val iconName = "ic_" + type.name.toLowerCase(Locale.ROOT)
            val resId = requireContext().resIdByName(iconName, "drawable")

            expenseTypeIcons.add(resources.getDrawable(resId, null))
        }

        adapter = ExpenseTypeAdapter(expenseTypeIcons)
        adapter.listener = ExpenseTypeAdapter.OnSelectExpenseTypeListener { expenseType ->
            selectedExpenseType = expenseType
        }
        binding.recViewExpenseTypes.adapter = adapter
    }

    private fun setOnTabSelectedListener() {
        binding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                val position = tab?.position
                position?.let {
                    if (position == 0) {
                        binding.recViewExpenseTypes.visibility = View.VISIBLE
                    } else {
                        binding.recViewExpenseTypes.visibility = View.INVISIBLE
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
        if (selectedExpenseType == null) {
            Toast.makeText(
                requireContext(),
                getString(R.string.select_category),
                Toast.LENGTH_SHORT
            ).show()
            return
        }

        val name: String = if (!binding.editTextNameOfExpense.text.isNullOrEmpty()) {
            binding.editTextNameOfExpense.text.toString()
        } else
            getString(R.string.expense)

        val addedSuccessfully = viewModel.addExpense(selectedExpenseType!!, name)
        if (addedSuccessfully) {
            sharedViewModel.changeFlagToSuccess()
        }
    }

    private fun addIncome() {
        val name: String = if (!binding.editTextNameOfExpense.text.isNullOrEmpty()) {
            binding.editTextNameOfExpense.text.toString()
        } else
            getString(R.string.income)

        val addedSuccessfully = viewModel.addIncome(name)
        if (addedSuccessfully) {
            sharedViewModel.changeFlagToSuccess()
        }
    }

    private fun observeInsertProcess() {
        viewModel.insertSuccess.observe(viewLifecycleOwner, { isSuccess ->
            if (isSuccess) {
                findNavController().popBackStack()
                viewModel.onInsertComplete()
            }
        })
    }
}
