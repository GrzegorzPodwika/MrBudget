package pl.podwikagrzegorz.mrbudget.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import pl.podwikagrzegorz.mrbudget.data.domain.*
import pl.podwikagrzegorz.mrbudget.databinding.HistoryBudgetViewBinding
import pl.podwikagrzegorz.mrbudget.other.asExpenseString
import pl.podwikagrzegorz.mrbudget.other.asIncomeString
import pl.podwikagrzegorz.mrbudget.other.asMonthWithYear
import java.text.SimpleDateFormat
import java.util.*

class BudgetAdapter(
    private val budgetClickListener: OnBudgetClickListener
) : ListAdapter<BudgetWithExpensesAndIncomes, BudgetAdapter.BudgetViewModel>(BudgetDiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BudgetViewModel =
        BudgetViewModel.from(parent)

    override fun onBindViewHolder(holder: BudgetViewModel, position: Int) {
        val budgetWithExpensesAndIncomes = getItem(position)

        holder.bind(budgetWithExpensesAndIncomes, budgetClickListener)
    }

    class BudgetViewModel private constructor(private val binding: HistoryBudgetViewBinding): RecyclerView.ViewHolder(binding.root) {

        fun bind(budgetWithExpensesAndIncomes: BudgetWithExpensesAndIncomes, budgetClickListener: OnBudgetClickListener) {
            val totalExpenses = budgetWithExpensesAndIncomes.expenses.stream().mapToDouble(Expense::value).sum()
            val totalIncomes = budgetWithExpensesAndIncomes.incomes.stream().mapToDouble(Income::value).sum()

            binding.apply {
                textViewDate.text = budgetWithExpensesAndIncomes.budget.date.asMonthWithYear()
                textViewHistoryExpenses.text = totalExpenses.asExpenseString()
                textViewHistoryIncomes.text = totalIncomes.asIncomeString()
            }
        }

        companion object {
            //private val dtf = SimpleDateFormat("MMM yyyy", Locale("pl"))

            fun from(parent: ViewGroup) : BudgetViewModel {
                val layoutManager = LayoutInflater.from(parent.context)
                val binding = HistoryBudgetViewBinding.inflate(layoutManager, parent, false)

                return BudgetViewModel(binding)
            }
        }
    }

    object BudgetDiffCallback : DiffUtil.ItemCallback<BudgetWithExpensesAndIncomes>() {

        override fun areItemsTheSame(
            oldItem: BudgetWithExpensesAndIncomes,
            newItem: BudgetWithExpensesAndIncomes
        ): Boolean {
            return oldItem.budget.budgetId == newItem.budget.budgetId
        }

        override fun areContentsTheSame(
            oldItem: BudgetWithExpensesAndIncomes,
            newItem: BudgetWithExpensesAndIncomes
        ): Boolean {
            return oldItem == newItem
        }
    }
}