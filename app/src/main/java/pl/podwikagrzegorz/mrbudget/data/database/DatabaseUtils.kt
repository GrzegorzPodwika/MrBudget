package pl.podwikagrzegorz.mrbudget.data.database

import pl.podwikagrzegorz.mrbudget.data.domain.Budget
import pl.podwikagrzegorz.mrbudget.data.domain.Expense
import pl.podwikagrzegorz.mrbudget.data.domain.ExpenseType

fun List<DatabaseExpense>.asDomainModel() : List<Expense> {
    return map {
        Expense(
            expenseId = it.expenseId,
            type = ExpenseType.valueOf(it.type),
            value = it.value
        )
    }
}

fun DatabaseBudget.asDomainModel() : Budget {
    return Budget(budgetId, date)
}