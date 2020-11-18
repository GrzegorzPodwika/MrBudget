package pl.podwikagrzegorz.mrbudget.ui.adapters

import pl.podwikagrzegorz.mrbudget.data.domain.Expense

interface EditDeleteListener {
    fun onEditClick(expense: Expense)
    fun onDeleteClick(expense: Expense)
}