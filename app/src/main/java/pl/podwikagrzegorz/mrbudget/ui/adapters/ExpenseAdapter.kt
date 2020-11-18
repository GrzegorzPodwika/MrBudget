package pl.podwikagrzegorz.mrbudget.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.chauthai.swipereveallayout.ViewBinderHelper
import com.google.android.material.chip.ChipGroup
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import pl.podwikagrzegorz.mrbudget.data.domain.Expense
import pl.podwikagrzegorz.mrbudget.data.domain.ExpenseType
import pl.podwikagrzegorz.mrbudget.databinding.ExpenseViewBinding

class ExpenseAdapter(
    private val editDeleteListener: EditDeleteListener
) : ListAdapter<Expense, ExpenseAdapter.ExpenseViewHolder>(ExpenseDiffCallback) {
    private val viewBinderHelper = ViewBinderHelper()
    private lateinit var originalListOfExpenses : List<Expense>
    private val adapterScope = CoroutineScope(Dispatchers.Default)

    fun submitListOfExpenses(listOfExpenses: List<Expense>){
        originalListOfExpenses = listOfExpenses

        submitList(originalListOfExpenses)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExpenseViewHolder {
        return ExpenseViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ExpenseViewHolder, position: Int) {
        val expense = getItem(position)
        viewBinderHelper.setOpenOnlyOne(true);
        viewBinderHelper.bind(holder.binding.swipeLayout, expense.expenseId.toString())
        viewBinderHelper.closeLayout(expense.expenseId.toString())

        holder.bind(expense, editDeleteListener)
    }

    fun filterList(expenseType: ExpenseType?) {
        adapterScope.launch {
            if (expenseType == null) {
                withContext(Dispatchers.Main) {
                    submitList(originalListOfExpenses)
                }
            } else {
                when(expenseType) {
                    ExpenseType.REGULAR -> {
                        val filteredList = originalListOfExpenses.filter { expense -> expense.type == ExpenseType.REGULAR }
                        withContext(Dispatchers.Main) {
                            submitList(filteredList)
                        }
                    }

                    ExpenseType.ONE_OFF -> {
                        val filteredList = originalListOfExpenses.filter { expense -> expense.type == ExpenseType.ONE_OFF }
                        withContext(Dispatchers.Main) {
                            submitList(filteredList)
                        }
                    }

                    ExpenseType.SAVINGS -> {
                        val filteredList = originalListOfExpenses.filter { expense -> expense.type == ExpenseType.SAVINGS }
                        withContext(Dispatchers.Main) {
                            submitList(filteredList)
                        }
                    }

                    ExpenseType.RETIREMENT -> {
                        val filteredList = originalListOfExpenses.filter { expense -> expense.type == ExpenseType.RETIREMENT }
                        withContext(Dispatchers.Main) {
                            submitList(filteredList)
                        }
                    }
                }
            }
        }
    }

    class ExpenseViewHolder private constructor(val binding: ExpenseViewBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(expense: Expense, editDeleteListener: EditDeleteListener) {
            binding.expense = expense
            binding.editdeleteListener = editDeleteListener
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): ExpenseViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ExpenseViewBinding.inflate(layoutInflater, parent, false)

                return ExpenseViewHolder(binding)
            }
        }
    }

    object ExpenseDiffCallback : DiffUtil.ItemCallback<Expense>() {
        override fun areItemsTheSame(oldItem: Expense, newItem: Expense): Boolean {
            return oldItem.expenseId == newItem.expenseId
        }

        override fun areContentsTheSame(oldItem: Expense, newItem: Expense): Boolean {
            return oldItem == newItem
        }

    }



}

