package pl.podwikagrzegorz.mrbudget.ui.adapters

import pl.podwikagrzegorz.mrbudget.data.domain.ExpenseType

interface OnSelectExpenseTypeListener {
    fun onTypeClick(expenseType: ExpenseType)
}