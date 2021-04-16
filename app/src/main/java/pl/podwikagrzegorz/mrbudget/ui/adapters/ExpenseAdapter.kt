package pl.podwikagrzegorz.mrbudget.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.chauthai.swipereveallayout.ViewBinderHelper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import pl.podwikagrzegorz.mrbudget.data.domain.Expense
import pl.podwikagrzegorz.mrbudget.data.domain.ExpenseType
import pl.podwikagrzegorz.mrbudget.databinding.ExpenseViewBinding

class ExpenseAdapter
    : ListAdapter<Expense, ExpenseAdapter.ExpenseViewHolder>(ExpenseDiffCallback) {

    private val viewBinderHelper = ViewBinderHelper()
    private lateinit var originalListOfExpenses: List<Expense>
    private val adapterScope = CoroutineScope(Dispatchers.Default)
    var listener: EditDeleteListener? = null

    interface EditDeleteListener {
        fun onEditClick(expense: Expense)
        fun onDeleteClick(expense: Expense)
    }

    fun submitListOfExpenses(listOfExpenses: List<Expense>) {
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

        holder.bind(expense, listener)
    }

    fun filterList(expenseType: ExpenseType?) {
        adapterScope.launch {
            if (expenseType == null) {
                withContext(Dispatchers.Main) {
                    submitList(originalListOfExpenses)
                }
            } else {
                when (expenseType) {
                    ExpenseType.GROCERIES -> {
                        val filteredList =
                            originalListOfExpenses.filter { expense -> expense.type == ExpenseType.GROCERIES }
                        withContext(Dispatchers.Main) {
                            submitList(filteredList)
                        }
                    }

                    ExpenseType.TRANSPORT -> {
                        val filteredList =
                            originalListOfExpenses.filter { expense -> expense.type == ExpenseType.TRANSPORT }
                        withContext(Dispatchers.Main) {
                            submitList(filteredList)
                        }
                    }

                    ExpenseType.HEALTH -> {
                        val filteredList =
                            originalListOfExpenses.filter { expense -> expense.type == ExpenseType.HEALTH }
                        withContext(Dispatchers.Main) {
                            submitList(filteredList)
                        }
                    }

                    ExpenseType.FAMILY -> {
                        val filteredList =
                            originalListOfExpenses.filter { expense -> expense.type == ExpenseType.FAMILY }
                        withContext(Dispatchers.Main) {
                            submitList(filteredList)
                        }
                    }

                    ExpenseType.GIFTS -> {
                        val filteredList =
                            originalListOfExpenses.filter { expense -> expense.type == ExpenseType.GIFTS }
                        withContext(Dispatchers.Main) {
                            submitList(filteredList)
                        }
                    }

                    ExpenseType.EDUCATION -> {
                        val filteredList =
                            originalListOfExpenses.filter { expense -> expense.type == ExpenseType.EDUCATION }
                        withContext(Dispatchers.Main) {
                            submitList(filteredList)
                        }
                    }

                    ExpenseType.HOME -> {
                        val filteredList =
                            originalListOfExpenses.filter { expense -> expense.type == ExpenseType.HOME }
                        withContext(Dispatchers.Main) {
                            submitList(filteredList)
                        }
                    }

                    ExpenseType.HOBBY -> {
                        val filteredList =
                            originalListOfExpenses.filter { expense -> expense.type == ExpenseType.HOBBY }
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

        fun bind(expense: Expense, listener: EditDeleteListener?) {
            binding.expense = expense
            listener?.let {
                binding.editdeleteListener = it
            }
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

