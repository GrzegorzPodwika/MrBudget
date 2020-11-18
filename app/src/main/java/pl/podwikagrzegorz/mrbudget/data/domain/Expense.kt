package pl.podwikagrzegorz.mrbudget.data.domain

data class Expense(
    val expenseId: Long,
    val budgetOwnerId: Long,
    val type: ExpenseType,
    val value: Double
)